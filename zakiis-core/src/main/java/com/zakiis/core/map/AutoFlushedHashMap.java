package com.zakiis.core.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoFlushedHashMap<K, V> extends SizedHashMap<K, V> {

	private static final long serialVersionUID = 1930109507293403128L;
	/** TTL for the entry, default is 5 minutes */
	protected static final Long DEFAULT_TTL = 5 * 60 * 1000L;
	/** expire time for the key in milliseconds */
	private final Map<K, Long> keyExpireTimeMap;
	/** time to live for the entry, in milliseconds */
	private final Long ttl;
	private final Function<K, V> flushMethod;
	
	public AutoFlushedHashMap(Function<K, V> flushMethod) {
		this.ttl = DEFAULT_TTL;
		this.keyExpireTimeMap = new HashMap<>(DEFAULT_THRESHOLD);
		this.flushMethod = flushMethod;
	}
	
	public AutoFlushedHashMap(int threshold, long ttl, Function<K, V> flushMethod) {
		super(threshold);
		this.ttl = ttl;
		this.keyExpireTimeMap = new HashMap<>(threshold);
		this.flushMethod = flushMethod;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		V v = super.get(key);
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
			put((K)key, v);
		}
		return v;
	}

	@Override
	public V put(K key, V value) {
		keyExpireTimeMap.put(key, System.currentTimeMillis() + ttl);
		return super.put(key, value);
	}

	@Override
	public V remove(Object key) {
		keyExpireTimeMap.remove(key);
		return super.remove(key);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
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

	private synchronized V flushValue(K key) {
		V v = super.get(key);
		if (v == null) {
			v = flushMethod.apply(key);
		}
		return v;
	}
}
