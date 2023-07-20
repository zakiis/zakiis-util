package com.zakiis.rpc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.Channel;

public interface RemotingServer {

	/**
     * server send sync request.
     *
     * @param resourceId rm client resourceId
     * @param clientId   rm client id
     * @param msg        message
     * @param tryOtherApp   try other app
     * @return client result message
     * @throws TimeoutException TimeoutException
     */
    Object sendSyncRequest(String resourceId, String clientId, Object msg, boolean tryOtherApp) throws TimeoutException;

    /**
     * server send sync request.
     *
     * @param channel client channel
     * @param msg     message
     * @return client result message
     * @throws TimeoutException TimeoutException
     */
    Object sendSyncRequest(Channel channel, Object msg) throws TimeoutException;

    /**
     * server send async request.
     *
     * @param channel client channel
     * @param msg     message
     */
    void sendAsyncRequest(Channel channel, Object msg);

    /**
     * server send async response.
     *
     * @param rpcMessage rpc message from client request
     * @param channel    client channel
     * @param msg        message
     */
    void sendAsyncResponse(RpcMessage rpcMessage, Channel channel, Object msg);

    /**
     * register processor
     *
     * @param messageType {@link io.seata.core.protocol.MessageType}
     * @param processor   {@link RemotingProcessor}
     * @param executor    thread pool
     */
    void registerProcessor(final int messageType, final RemotingProcessor processor, final ExecutorService executor);
}
