package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.exception.TransactionExceptionCode;
import com.zakiis.rpc.protocol.AbstractResultMessage;

public abstract class AbstractTransactionResponse extends AbstractResultMessage {

	private static final long serialVersionUID = -5880399926240887928L;
	
	private TransactionExceptionCode transactionExceptionCode = TransactionExceptionCode.Unknown;

    /**
     * Gets transaction exception code.
     *
     * @return the transaction exception code
     */
    public TransactionExceptionCode getTransactionExceptionCode() {
        return transactionExceptionCode;
    }

    /**
     * Sets transaction exception code.
     *
     * @param transactionExceptionCode the transaction exception code
     */
    public void setTransactionExceptionCode(TransactionExceptionCode transactionExceptionCode) {
        this.transactionExceptionCode = transactionExceptionCode;
    }

}
