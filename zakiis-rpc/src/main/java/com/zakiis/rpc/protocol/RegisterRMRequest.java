package com.zakiis.rpc.protocol;

import java.io.Serializable;


public class RegisterRMRequest extends AbstractIdentifyRequest implements Serializable {

	private static final long serialVersionUID = 2142827439911295726L;

	/**
     * Instantiates a new Register rm request.
     */
    public RegisterRMRequest() {
        this(null, null);
    }

    /**
     * Instantiates a new Register rm request.
     *
     * @param applicationId           the application id
     * @param transactionServiceGroup the transaction service group
     */
    public RegisterRMRequest(String applicationId, String transactionServiceGroup) {
        super(applicationId, transactionServiceGroup);
    }

    private String resourceIds;

    /**
     * Gets resource ids.
     *
     * @return the resource ids
     */
    public String getResourceIds() {
        return resourceIds;
    }

    /**
     * Sets resource ids.
     *
     * @param resourceIds the resource ids
     */
    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_RM;
    }

    @Override
    public String toString() {
        return "RegisterRMRequest{" +
                "resourceIds='" + resourceIds + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", transactionServiceGroup='" + transactionServiceGroup + '\'' +
                '}';
    }
}
