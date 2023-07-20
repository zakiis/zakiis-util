package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.MessageType;
import com.zakiis.rpc.protocol.transaction.constant.GlobalStatus;

public class GlobalReportRequest extends AbstractGlobalEndRequest {

	private static final long serialVersionUID = -8489271163410811107L;
	/**
     * The Global status.
     */
    protected GlobalStatus globalStatus;

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_REPORT;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

    /**
     * Gets global status.
     *
     * @return the global status
     */
    public GlobalStatus getGlobalStatus() {
        return globalStatus;
    }

    /**
     * Sets global status.
     *
     * @param globalStatus the global status
     */
    public void setGlobalStatus(GlobalStatus globalStatus) {
        this.globalStatus = globalStatus;
    }
}
