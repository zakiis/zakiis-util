package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.transaction.constant.GlobalStatus;

public abstract class AbstractGlobalEndResponse extends AbstractTransactionResponse {

	private static final long serialVersionUID = 3514726054074294378L;
	/**
     * The Global status.
     */
    protected GlobalStatus globalStatus;

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("globalStatus=");
        result.append(globalStatus);
        result.append(",");
        result.append("ResultCode=");
        result.append(getResultCode());
        result.append(",");
        result.append("Msg=");
        result.append(getMsg());

        return result.toString();
    }

}