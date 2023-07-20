package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.MessageType;

public class GlobalCommitRequest extends AbstractGlobalEndRequest {

	private static final long serialVersionUID = -7968011902894825427L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_COMMIT;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }
}
