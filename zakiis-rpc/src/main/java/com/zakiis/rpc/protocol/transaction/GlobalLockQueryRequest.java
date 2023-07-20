package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.MessageType;

public class GlobalLockQueryRequest extends BranchRegisterRequest  {

	private static final long serialVersionUID = -5731882811881723511L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_LOCK_QUERY;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

}
