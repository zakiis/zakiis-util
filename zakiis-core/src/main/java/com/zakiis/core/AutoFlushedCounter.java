package com.zakiis.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class AutoFlushedCounter {
	
	Logger log = LoggerFactory.getLogger(AutoFlushedCounter.class);

	/** 达到maxFlushSize时，立刻调用flushMethod */
	private final Integer maxFlushSize;
	/** 刷新数据间隔(毫秒) */
	private final Long flushIntervalMills;
	/** 持久化方法 */
	private final BiConsumer<Object, Long> flushMethod;
	/** 当前剩余刷新的值 */
	private Map<Object, AtomicLong> currentFlushMap = new HashMap<Object, AtomicLong>();
	
	public AutoFlushedCounter(Integer maxFlushSize, Long flushIntervalMills, BiConsumer<Object, Long> flushMethod) {
		this.maxFlushSize = maxFlushSize;
		this.flushIntervalMills = flushIntervalMills;
		this.flushMethod = flushMethod;
		startFlushTask();
	}
	
	public long incre(Object key) {
		if (!currentFlushMap.containsKey(key)) {
			currentFlushMap.put(key, new AtomicLong(0L));
		}
		AtomicLong atomicLong = currentFlushMap.get(key);
		long value = atomicLong.incrementAndGet();
		increNotFlushedCount(key);
		if (value >= maxFlushSize) {
			flushData(key, value);
			boolean isSetSuccess = atomicLong.compareAndSet(value, 0L);
			if (!isSetSuccess) {
				throw new ZakiisRuntimeException("value changed when set value");
			}
			value = 0;
		}
		return value;
	}
	
	public void startFlushTask() {
		new Thread(() -> {
			while (true) {
				try {
					List<Entry<Object, AtomicLong>> needFlushEntries = currentFlushMap.entrySet().stream().filter(e -> e.getValue().longValue() >= 0).collect(Collectors.toList());
					for (Entry<Object, AtomicLong> entry : needFlushEntries) {
						flushData(entry.getKey(), entry.getValue().longValue());
						currentFlushMap.remove(entry.getKey());
					}
					Thread.sleep(flushIntervalMills);
				} catch (Throwable e) {
					log.warn("call flush method got an exception", e);
				}
			}
		}).start();
	}
	
	public synchronized void flushData(Object key, Long value) {
		flushMethod.accept(key, value);
		decreNotFlushedCount(key, value);
	}
	
	// 分布式环境下，应当使用redis来重写此方法
	public void increNotFlushedCount(Object key) {
		
	}
	
	// 分布式环境下，应当使用redis来重写此方法
	public void decreNotFlushedCount(Object key, long count) {
		
	}
	
	// 分布式环境下，应当使用redis来重写此方法
	public long getNotFlushedCount(Object key) {
		if (!currentFlushMap.containsKey(key)) {
			return 0L;
		}
		return currentFlushMap.get(key).longValue();
	}
}
