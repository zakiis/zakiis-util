package com.zakiis.core;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisAutoFlushedCounter extends AutoFlushedCounter {
	
	private final RedisTemplate<String, Object> redisTemplate;
	/** 给个prefix可以防止不同的Counter实例数据重叠了 */
	private final String keySuffix;
	
	public RedisAutoFlushedCounter(Integer maxFlushSize, Long flushIntervalMills
			, BiConsumer<Object, Long> flushMethod, RedisTemplate<String, Object> redisTemplate) {
		this(maxFlushSize, flushIntervalMills, flushMethod, redisTemplate, RandomStringUtils.randomAlphabetic(6));
	}
	
	public RedisAutoFlushedCounter(Integer maxFlushSize, Long flushIntervalMills
			, BiConsumer<Object, Long> flushMethod, RedisTemplate<String, Object> redisTemplate
			, String keySuffix) {
		super(maxFlushSize, flushIntervalMills, flushMethod);
		this.redisTemplate = redisTemplate;
		this.keySuffix = keySuffix;
	}

	@Override
	public void increNotFlushedCount(Object key) {
		String redisKey = getRedisKey(key);
		redisTemplate.opsForValue().increment(redisKey);
	}

	@Override
	public void decreNotFlushedCount(Object key, long count) {
		String redisKey = getRedisKey(key);
		Long newValue = redisTemplate.opsForValue().decrement(redisKey, count);
		if (newValue != null && newValue <= 0) {
			redisTemplate.delete(key.toString());
		}
	}

	@Override
	public long getNotFlushedCount(Object key) {
		String redisKey = getRedisKey(key);
		Object value = redisTemplate.opsForValue().get(redisKey);
		if (value == null) {
			return 0;
		}
		return ((Number)value).longValue();
	}
	
	private String getRedisKey(Object key) {
		return key.toString() + "_" + keySuffix;
	}

	
}
