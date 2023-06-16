package com.zakiis.core.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sized Hash Map 
 * @date 2023-06-15 17:29:28
 * @author Liu Zhenghua
 */
public class SizedHashMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {
	
	private static final long serialVersionUID = -8850467900003431015L;
	protected static final int DEFAULT_THRESHOLD = 10;
	/** The threshold of the hash map */
	private final Integer threshold;
	
	public SizedHashMap() {
		threshold = DEFAULT_THRESHOLD;
	}
	
	public SizedHashMap(Integer threshold) {
		this.threshold = threshold;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return super.size() > threshold;
	}

}
