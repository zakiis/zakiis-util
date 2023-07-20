package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.protocol.MessageType;

public class GlobalStatusRequest extends AbstractGlobalEndRequest {

	private static final long serialVersionUID = -223241359482637648L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_STATUS;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }
}
