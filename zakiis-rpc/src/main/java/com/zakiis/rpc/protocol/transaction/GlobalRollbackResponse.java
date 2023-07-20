package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;

public class GlobalRollbackResponse extends AbstractGlobalEndResponse {

	private static final long serialVersionUID = -2766948423177445616L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_ROLLBACK_RESULT;
    }
}
