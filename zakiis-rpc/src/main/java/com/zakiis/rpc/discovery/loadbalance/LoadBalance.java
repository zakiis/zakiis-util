package com.zakiis.rpc.discovery.loadbalance;

import java.util.List;

/**
 * The interface Load balance.
 *
 * @author slievrly
 */
public interface LoadBalance {

    /**
     * Select t.
     *
     * @param <T>      the type parameter
     * @param invokers the invokers
     * @param xid      the xid
     * @return the t
     * @throws Exception the exception
     */
    <T> T select(List<T> invokers, String xid) throws Exception;
}
