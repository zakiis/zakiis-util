package com.zakiis.rpc.protocol;


public class MergeResultMessage extends AbstractMessage implements MergeMessage {

	private static final long serialVersionUID = -8230997007176506709L;
	/**
     * The Msgs.
     */
    public AbstractResultMessage[] msgs;

    /**
     * Get msgs abstract result message [ ].
     *
     * @return the abstract result message [ ]
     */
    public AbstractResultMessage[] getMsgs() {
        return msgs;
    }

    /**
     * Sets msgs.
     *
     * @param msgs the msgs
     */
    public void setMsgs(AbstractResultMessage[] msgs) {
        this.msgs = msgs;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_SEATA_MERGE_RESULT;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MergeResultMessage ");
        if (msgs == null) {
            return sb.toString();
        }
        for (AbstractMessage msg : msgs) { sb.append(msg.toString()).append("\n"); }
        return sb.toString();
    }
}
