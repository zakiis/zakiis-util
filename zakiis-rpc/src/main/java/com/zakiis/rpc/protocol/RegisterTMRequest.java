package com.zakiis.rpc.protocol;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.zakiis.core.util.NetUtil;
import com.zakiis.rpc.config.ConfigurationKeys;

public class RegisterTMRequest extends AbstractIdentifyRequest implements Serializable {
    private static final long serialVersionUID = -5929081344190543690L;
    public static final String UDATA_VGROUP = "vgroup";
    public static final String UDATA_AK = "ak";
    public static final String UDATA_DIGEST = "digest";
    public static final String UDATA_IP = "ip";
    public static final String UDATA_TIMESTAMP = "timestamp";
    public static final String UDATA_AUTH_VERSION = "authVersion";

    /**
     * Instantiates a new Register tm request.
     */
    public RegisterTMRequest() {
        this(null, null);
    }

    /**
     * Instantiates a new Register tm request.
     *
     * @param applicationId           the application id
     * @param transactionServiceGroup the transaction service group
     * @param extraData               the extra data
     */
    public RegisterTMRequest(String applicationId, String transactionServiceGroup, String extraData) {
        super(applicationId, transactionServiceGroup, extraData);
        StringBuilder sb = new StringBuilder();
        if (null != extraData) {
            sb.append(extraData);
            if (!extraData.endsWith(ConfigurationKeys.EXTRA_DATA_SPLIT_CHAR)) {
                sb.append(ConfigurationKeys.EXTRA_DATA_SPLIT_CHAR);
            }
        }
        if (transactionServiceGroup != null && !transactionServiceGroup.isEmpty()) {
            sb.append(String.format("%s=%s", UDATA_VGROUP, transactionServiceGroup));
            sb.append(ConfigurationKeys.EXTRA_DATA_SPLIT_CHAR);
            String clientIP = NetUtil.getLocalIp();
            if (!StringUtils.isEmpty(clientIP)) {
                sb.append(String.format("%s=%s", UDATA_IP, clientIP));
                sb.append(ConfigurationKeys.EXTRA_DATA_SPLIT_CHAR);
            }
        }
        this.extraData = sb.toString();

    }

    /**
     * Instantiates a new Register tm request.
     *
     * @param applicationId           the application id
     * @param transactionServiceGroup the transaction service group
     */
    public RegisterTMRequest(String applicationId, String transactionServiceGroup) {
        this(applicationId, transactionServiceGroup, null);
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_CLT;
    }

    @Override
    public String toString() {
        return "RegisterTMRequest{" +
            "applicationId='" + applicationId + '\'' +
            ", transactionServiceGroup='" + transactionServiceGroup + '\'' +
            '}';
    }
}
