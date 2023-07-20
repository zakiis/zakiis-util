package com.zakiis.rpc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.AbstractMessage;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.Channel;


/**
 * The interface of remoting client.
 * @date 2023-07-06 09:46:15
 * @author Liu Zhenghua
 */
public interface RemotingClient {

	/**
     * client send sync request.
     * @param msg message
     * @return server result message
     * @throws TimeoutException TimeoutException
     */
    Object sendSyncRequest(Object msg) throws TimeoutException;

    /**
     * client send sync request.
     *
     * @param channel client channel
     * @param msg message
     * @return server result message
     * @throws TimeoutException TimeoutException
     */
    Object sendSyncRequest(Channel channel, Object msg) throws TimeoutException;

    /**
     * client send async request.
     * @param channel client channel
     * @param msg     message 
     */
    void sendAsyncRequest(Channel channel, Object msg);

    /**
     * client send async response.
     *
     * @param serverAddress server address
     * @param rpcMessage    rpc message from server request
     * @param msg           message 
     */
    void sendAsyncResponse(String serverAddress, RpcMessage rpcMessage, Object msg);

    /**
     * On register msg success.
     *
     * @param serverAddress  the server address
     * @param channel        the channel
     * @param response       the response
     * @param requestMessage the request message
     */
    void onRegisterMsgSuccess(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage);

    /**
     * On register msg fail.
     *
     * @param serverAddress  the server address
     * @param channel        the channel
     * @param response       the response
     * @param requestMessage the request message
     */
    void onRegisterMsgFail(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage);

    /**
     * register processor
     *
     * @param messageType {@link io.seata.core.protocol.MessageType}
     * @param processor   {@link RemotingProcessor}
     * @param executor    thread pool
     */
    void registerProcessor(final int messageType, final RemotingProcessor processor, final ExecutorService executor);
}
