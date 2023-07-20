package com.zakiis.rpc.protocol;

import java.io.Serializable;


public class RegisterRMResponse extends AbstractIdentifyResponse implements Serializable {

	private static final long serialVersionUID = -7366217935731359453L;

	/**
     * Instantiates a new Register rm response.
     */
    public RegisterRMResponse() {
        this(true);
    }

    /**
     * Instantiates a new Register rm response.
     *
     * @param result the result
     */
    public RegisterRMResponse(boolean result) {
        super();
        setIdentified(result);
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_RM_RESULT;
    }
}
