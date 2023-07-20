package com.zakiis.rpc.protocol;

import java.util.ArrayList;
import java.util.List;


public class BatchResultMessage extends AbstractMessage {

	private static final long serialVersionUID = -2447488941607096869L;

	/**
     * the result messages
     */
    private List<AbstractResultMessage> resultMessages = new ArrayList<>();

    /**
     * the message Ids
     */
    private List<Integer> msgIds = new ArrayList<>();

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_BATCH_RESULT_MSG;
    }

    public List<AbstractResultMessage> getResultMessages() {
        return resultMessages;
    }

    public void setResultMessages(List<AbstractResultMessage> resultMessages) {
        this.resultMessages = resultMessages;
    }

    public List<Integer> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(List<Integer> msgIds) {
        this.msgIds = msgIds;
    }
}
