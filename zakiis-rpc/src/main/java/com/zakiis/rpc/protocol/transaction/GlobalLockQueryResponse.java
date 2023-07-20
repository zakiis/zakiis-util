package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;

public class GlobalLockQueryResponse extends AbstractTransactionResponse {

	private static final long serialVersionUID = -5304461644535614337L;
	private boolean lockable = false;

    /**
     * Is lockable boolean.
     *
     * @return the boolean
     */
    public boolean isLockable() {
        return lockable;
    }

    /**
     * Sets lockable.
     *
     * @param lockable the lockable
     */
    public void setLockable(boolean lockable) {
        this.lockable = lockable;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_LOCK_QUERY_RESULT;
    }

}
