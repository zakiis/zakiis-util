package com.zakiis.rpc.protocol.transaction;

import com.zakiis.rpc.protocol.MessageType;

public class GlobalBeginResponse extends AbstractTransactionResponse {

	private static final long serialVersionUID = 6598634851365443120L;

	private String xid;

    private String extraData;

    /**
     * Gets xid.
     *
     * @return the xid
     */
    public String getXid() {
        return xid;
    }

    /**
     * Sets xid.
     *
     * @param xid the xid
     */
    public void setXid(String xid) {
        this.xid = xid;
    }

    /**
     * Gets extra data.
     *
     * @return the extra data
     */
    public String getExtraData() {
        return extraData;
    }

    /**
     * Sets extra data.
     *
     * @param extraData the extra data
     */
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_BEGIN_RESULT;
    }

}