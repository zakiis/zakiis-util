package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;

public class BranchReportResponse extends AbstractTransactionResponse {

	private static final long serialVersionUID = -4311372618686979955L;

	@Override
    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_STATUS_REPORT_RESULT;
    }
}
