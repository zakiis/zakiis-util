package com.zakiis.core;

import java.util.HashMap;
import java.util.Map;

public class ThreadContext {
	
	private final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>() {

		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
		
	};

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T)threadLocal.get().get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T put(String key, T value) {
		T oldValue = (T)threadLocal.get().get(key);
		threadLocal.get().put(key, value);
		return oldValue;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T remove(String key) {
		return (T)threadLocal.get().remove(key);
	}
	
	public void clear() {
		threadLocal.remove();
	}
}
