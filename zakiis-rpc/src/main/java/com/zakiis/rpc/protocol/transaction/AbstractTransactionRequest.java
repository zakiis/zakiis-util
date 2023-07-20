package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.AbstractMessage;

public abstract class AbstractTransactionRequest extends AbstractMessage {

	private static final long serialVersionUID = -7190229025429654721L;

	/**
     * Handle abstract transaction response.
     *
     * @param rpcContext the rpc context
     * @return the abstract transaction response
     */
    public abstract AbstractTransactionResponse handle(RpcContext rpcContext);
}
