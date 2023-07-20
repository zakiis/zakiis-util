package com.zakiis.rpc.protocol;


public abstract class AbstractResultMessage extends AbstractMessage  {

	private static final long serialVersionUID = 9126065482735080648L;

	private ResultCode resultCode;

    private String msg;

    /**
     * Gets result code.
     *
     * @return the result code
     */
    public ResultCode getResultCode() {
        return resultCode;
    }

    /**
     * Sets result code.
     *
     * @param resultCode the result code
     */
    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
