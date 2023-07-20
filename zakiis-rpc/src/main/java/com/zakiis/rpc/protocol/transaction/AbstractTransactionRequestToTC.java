package com.zakiis.rpc.protocol.transaction;


public abstract class AbstractTransactionRequestToTC extends AbstractTransactionRequest {

	private static final long serialVersionUID = -6824461569277333859L;
	/**
     * The Handler.
     */
    protected TCInboundHandler handler;

    /**
     * Sets tc inbound handler.
     *
     * @param handler the handler
     */
    public void setTCInboundHandler(TCInboundHandler handler) {
        this.handler = handler;
    }
}
