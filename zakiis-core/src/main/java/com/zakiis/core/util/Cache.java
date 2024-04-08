package com.zakiis.core.util;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Callable;
  
@Slf4j
public class Cache<T> {

    private final long ttl;
    private final Callable<T> loader;
    private Long lastUpdateTime;
    private T value;

    public Cache(final long ttl, Callable<T> loader) {
        this.ttl = ttl;
        this.loader = loader;
    }

    public T get() {
        if (value == null || System.currentTimeMillis() - lastUpdateTime > ttl) {
            synchronized (this) {
                if (value == null || System.currentTimeMillis() - lastUpdateTime > ttl) {
                    try {
                        value = loader.call();
                        lastUpdateTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        log.warn("refresh cache data got an exception", e);
                    }
                }
            }
        }
        return value;
    }

}
