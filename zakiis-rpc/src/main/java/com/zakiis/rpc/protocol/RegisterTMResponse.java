package com.zakiis.rpc.protocol;

import java.io.Serializable;


public class RegisterTMResponse extends AbstractIdentifyResponse implements Serializable {

	private static final long serialVersionUID = -3181530852051070186L;

	/**
     * Instantiates a new Register tm response.
     */
    public RegisterTMResponse() {
        this(true);
    }

    /**
     * Instantiates a new Register tm response.
     *
     * @param result the result
     */
    public RegisterTMResponse(boolean result) {
        super();
        setIdentified(result);
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_CLT_RESULT;
    }
}
