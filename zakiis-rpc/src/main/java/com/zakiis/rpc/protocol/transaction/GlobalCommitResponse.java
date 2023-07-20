package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;


public class GlobalCommitResponse extends AbstractGlobalEndResponse {

	private static final long serialVersionUID = -6628632168102858657L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_COMMIT_RESULT;
    }
}
