package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;

public class GlobalStatusResponse extends AbstractGlobalEndResponse {

	private static final long serialVersionUID = 290680349397139262L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_STATUS_RESULT;
    }
}
