package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;

public class GlobalReportResponse extends AbstractGlobalEndResponse {

	private static final long serialVersionUID = 5183795368070118983L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_REPORT_RESULT;
    }
}
