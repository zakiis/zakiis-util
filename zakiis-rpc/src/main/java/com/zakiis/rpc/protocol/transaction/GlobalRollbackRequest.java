package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.MessageType;

public class GlobalRollbackRequest extends AbstractGlobalEndRequest {

	private static final long serialVersionUID = -7924016030514669533L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_ROLLBACK;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }
}
