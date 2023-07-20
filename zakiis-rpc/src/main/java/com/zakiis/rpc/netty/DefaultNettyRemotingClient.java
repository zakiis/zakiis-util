package com.zakiis.rpc.netty;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.rpc.DefaultValues;
import com.zakiis.rpc.config.ConfigurationCache;
import com.zakiis.rpc.config.ConfigurationChangeEvent;
import com.zakiis.rpc.config.ConfigurationChangeListener;
import com.zakiis.rpc.config.ConfigurationFactory;
import com.zakiis.rpc.config.ConfigurationKeys;
import com.zakiis.rpc.exception.FrameworkErrorCode;
import com.zakiis.rpc.exception.FrameworkException;
import com.zakiis.rpc.netty.NettyPoolKey.TransactionRole;
import com.zakiis.rpc.netty.thread.NamedThreadFactory;
import com.zakiis.rpc.processor.client.ClientHeartbeatProcessor;
import com.zakiis.rpc.protocol.AbstractMessage;
import com.zakiis.rpc.protocol.MessageType;
import com.zakiis.rpc.protocol.RegisterRMRequest;
import com.zakiis.rpc.protocol.RegisterRMResponse;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutorGroup;

public class DefaultNettyRemotingClient extends AbstractNettyRemotingClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNettyRemotingClient.class);
    private static final String DBKEYS_SPLIT_CHAR = ",";
    private static volatile DefaultNettyRemotingClient instance;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final long KEEP_ALIVE_TIME = Integer.MAX_VALUE;
    private static final int MAX_QUEUE_SIZE = 20000;
    private String applicationId;
    private String transactionServiceGroup;

    @Override
    public void init() {
        // registry processor
        registerProcessor();
        if (initialized.compareAndSet(false, true)) {
            super.init();
        }
    }

    private DefaultNettyRemotingClient(NettyClientConfig nettyClientConfig, EventExecutorGroup eventExecutorGroup,
                                  ThreadPoolExecutor messageExecutor) {
        super(nettyClientConfig, eventExecutorGroup, messageExecutor, TransactionRole.RMROLE);
        // set enableClientBatchSendRequest
        this.enableClientBatchSendRequest = ConfigurationFactory.getInstance().getBoolean(ConfigurationKeys.ENABLE_RM_CLIENT_BATCH_SEND_REQUEST,
                ConfigurationFactory.getInstance().getBoolean(ConfigurationKeys.ENABLE_CLIENT_BATCH_SEND_REQUEST,DefaultValues.DEFAULT_ENABLE_RM_CLIENT_BATCH_SEND_REQUEST));
        ConfigurationCache.addConfigListener(ConfigurationKeys.ENABLE_RM_CLIENT_BATCH_SEND_REQUEST, new ConfigurationChangeListener() {
            @Override
            public void onChangeEvent(ConfigurationChangeEvent event) {
                String dataId = event.getDataId();
                String newValue = event.getNewValue();
                if (ConfigurationKeys.ENABLE_RM_CLIENT_BATCH_SEND_REQUEST.equals(dataId) && StringUtils.isNotBlank(newValue)) {
                    enableClientBatchSendRequest = Boolean.parseBoolean(newValue);
                }
            }
        });
    }

    /**
     * Gets instance.
     *
     * @param applicationId           the application id
     * @param transactionServiceGroup the transaction service group
     * @return the instance
     */
    public static DefaultNettyRemotingClient getInstance(String applicationId, String transactionServiceGroup) {
    	DefaultNettyRemotingClient nettyRemotingClient = getInstance();
        nettyRemotingClient.setApplicationId(applicationId);
        nettyRemotingClient.setTransactionServiceGroup(transactionServiceGroup);
        return nettyRemotingClient;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static DefaultNettyRemotingClient getInstance() {
        if (instance == null) {
            synchronized (DefaultNettyRemotingClient.class) {
                if (instance == null) {
                    NettyClientConfig nettyClientConfig = new NettyClientConfig();
                    final ThreadPoolExecutor messageExecutor = new ThreadPoolExecutor(
                        nettyClientConfig.getClientWorkerThreads(), nettyClientConfig.getClientWorkerThreads(),
                        KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
                        new NamedThreadFactory(nettyClientConfig.getRmDispatchThreadPrefix(),
                            nettyClientConfig.getClientWorkerThreads()), new ThreadPoolExecutor.CallerRunsPolicy());
                    instance = new DefaultNettyRemotingClient(nettyClientConfig, null, messageExecutor);
                }
            }
        }
        return instance;
    }

    /**
     * Sets application id.
     *
     * @param applicationId the application id
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Sets transaction service group.
     *
     * @param transactionServiceGroup the transaction service group
     */
    public void setTransactionServiceGroup(String transactionServiceGroup) {
        this.transactionServiceGroup = transactionServiceGroup;
    }


    @Override
    public void onRegisterMsgSuccess(String serverAddress, Channel channel, Object response,
                                     AbstractMessage requestMessage) {
        RegisterRMRequest registerRMRequest = (RegisterRMRequest)requestMessage;
        RegisterRMResponse registerRMResponse = (RegisterRMResponse)response;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("register RM success. client version:{}, server version:{},channel:{}", registerRMRequest.getVersion(), registerRMResponse.getVersion(), channel);
        }
        getClientChannelManager().registerChannel(serverAddress, channel);

    }

    @Override
    public void onRegisterMsgFail(String serverAddress, Channel channel, Object response,
                                  AbstractMessage requestMessage) {
        RegisterRMRequest registerRMRequest = (RegisterRMRequest)requestMessage;
        RegisterRMResponse registerRMResponse = (RegisterRMResponse)response;
        String errMsg = String.format(
            "register RM failed. client version: %s,server version: %s, errorMsg: %s, " + "channel: %s", registerRMRequest.getVersion(), registerRMResponse.getVersion(), registerRMResponse.getMsg(), channel);
        throw new FrameworkException(errMsg);
    }


    @Override
    public void destroy() {
        super.destroy();
        initialized.getAndSet(false);
        instance = null;
    }


    @Override
    protected String getTransactionServiceGroup() {
        return transactionServiceGroup;
    }

    @Override
    public boolean isEnableClientBatchSendRequest() {
        return enableClientBatchSendRequest;
    }

    @Override
    public long getRpcRequestTimeout() {
        return NettyClientConfig.getRpcRmRequestTimeout();
    }

    private void registerProcessor() {
        // registry heartbeat message processor
        ClientHeartbeatProcessor clientHeartbeatProcessor = new ClientHeartbeatProcessor();
        super.registerProcessor(MessageType.TYPE_HEARTBEAT_MSG, clientHeartbeatProcessor, null);
    }

}
