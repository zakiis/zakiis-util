package com.zakiis.core.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

@Slf4j
public class ReactorAutoFlushedHashMap<K, V> extends SizedHashMap<K, Mono<V>> {

	private static final long serialVersionUID = 1930109507293403128L;
	/** TTL for the entry, default is 5 minutes */
	protected static final Long DEFAULT_TTL = 5 * 60 * 1000L;
	/** expire time for the key in milliseconds */
	private final  Map<K, Long> keyExpireTimeMap;
	/** time to live for the entry, in milliseconds */
	private final Long ttl;
	private final Function<K, Mono<V>> flushMethod;
	private volatile Map<K, List<MonoSink<V>>> needEmitMonoSinkMap = new ConcurrentHashMap<K, List<MonoSink<V>>>();
	
	public ReactorAutoFlushedHashMap(Function<K, Mono<V>> flushMethod) {
		this.ttl = DEFAULT_TTL;
		this.keyExpireTimeMap = new HashMap<>(DEFAULT_THRESHOLD);
		this.flushMethod = flushMethod;
	}
	
	public ReactorAutoFlushedHashMap(int threshold, long ttl, Function<K, Mono<V>> flushMethod) {
		super(threshold);
		this.ttl = ttl;
		this.keyExpireTimeMap = new HashMap<>(threshold);
		this.flushMethod = flushMethod;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Mono<V> get(Object key) {
		Mono<V> v = super.get(key);
		Long expireTime = keyExpireTimeMap.get(key);
		boolean needFlushValue = false;
		if (expireTime == null) {
			needFlushValue = true;
		} else if (expireTime < System.currentTimeMillis()) {
			log.debug("key {} expired, would be evicted.");
			remove(key);
			needFlushValue = true;
		}
		if (needFlushValue) {
			v = flushValue((K)key);
		}
		return v;
	}

	/**
	 * put method should called by get method, not call it externally.
	 */
	@Override
	@Deprecated
	public Mono<V> put(K key, Mono<V> value) {
		keyExpireTimeMap.put(key, System.currentTimeMillis() + ttl);
		return super.put(key, value);
	}

	@Override
	public Mono<V> remove(Object key) {
		keyExpireTimeMap.remove(key);
		return super.remove(key);
	}

	@Override
	public Set<Map.Entry<K, Mono<V>>> entrySet() {
		Set<K> keySet = super.keySet();
		Set<K> expiredKeySet = keySet.stream().filter(k -> {
			Long expireTime = keyExpireTimeMap.get(k);
			return expireTime < System.currentTimeMillis();
		}).collect(Collectors.toSet());
		for (K key : expiredKeySet) {
			remove(key);
		}
		return super.entrySet();
	}

	private synchronized Mono<V> flushValue(K key) {
		Mono<V> v = super.get(key);
		if (v == null) {
			// retrieve value from flushMethod and emit the value to the Mono that hold by other request.
			v = flushMethod.apply(key).doOnNext(actualValue -> {
				put(key, Mono.just(actualValue));
				List<MonoSink<V>> sinkList = needEmitMonoSinkMap.get(key);
				if (sinkList != null) {
					sinkList.forEach(sink -> sink.success(actualValue));
					needEmitMonoSinkMap.remove(key);
				}
			}).doOnError(t -> {
				List<MonoSink<V>> sinkList = needEmitMonoSinkMap.get(key);
				if (sinkList != null) {
					sinkList.forEach(sink -> sink.error(t));
					needEmitMonoSinkMap.remove(key);
				}
			});
			// put Mono.empty() to avoid multiple flushValue executed by threads.
			put(key, Mono.empty());
		} else {
			if (v == Mono.empty()) {
				v = Mono.create(sink -> {
					// executed when subscribed, flushValue method has been exited so there need a lock
					synchronized (needEmitMonoSinkMap) {
						List<MonoSink<V>> sinkList = needEmitMonoSinkMap.get(key);
						if (sinkList == null) {
							sinkList = new ArrayList<MonoSink<V>>();
							needEmitMonoSinkMap.put(key, sinkList);
						}
						sinkList.add(sink);
					}
				});
			}
		}
		return v;
	}
}
