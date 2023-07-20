package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.MessageType;

public class GlobalBeginRequest extends AbstractTransactionRequestToTC {

	private static final long serialVersionUID = 5016919089055945936L;

	private int timeout = 60000;

    private String transactionName;

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets transaction name.
     *
     * @return the transaction name
     */
    public String getTransactionName() {
        return transactionName;
    }

    /**
     * Sets transaction name.
     *
     * @param transactionName the transaction name
     */
    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_BEGIN;
    }


    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("timeout=");
        result.append(timeout);
        result.append(",");
        result.append("transactionName=");
        result.append(transactionName);

        return result.toString();
    }
}