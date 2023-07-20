package com.zakiis.rpc.config;

import java.util.concurrent.ExecutorService;

public interface ConfigChangeListener {

    /**
     * Gets executor.
     *
     * @return the executor
     */
    ExecutorService getExecutor();

    /**
     * Receive config info.
     *
     * @param configInfo the config info
     */
    void receiveConfigInfo(final String configInfo);
}