package com.zakiis.rpc.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class MergedWrapMessage extends AbstractMessage implements Serializable, MergeMessage {

	private static final long serialVersionUID = -5642707903172777308L;
	/**
     * The Msgs.
     */
    public List<AbstractMessage> msgs = new ArrayList<>();
    /**
     * The Msg ids.
     */
    public List<Integer> msgIds = new ArrayList<>();

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_SEATA_MERGE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SeataMergeMessage ");
        for (AbstractMessage msg : msgs) {
            sb.append(msg.toString()).append("\n");
        }
        return sb.toString();
    }
}
