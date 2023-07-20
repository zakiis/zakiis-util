package com.zakiis.rpc;

/**
 * The boot strap of the remoting process, generally there are client and server implementation classes
 * @date 2023-07-06 09:43:17
 * @author Liu Zhenghua
 */
public interface RemotingBootstrap {

	/**
     * Start.
     */
    void start();

    /**
     * Shutdown.
     */
    void shutdown();
}
