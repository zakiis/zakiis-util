package com.zakiis.rpc.processor;

import com.zakiis.rpc.TransactionMessageHandler;
import com.zakiis.rpc.processor.client.ClientOnResponseProcessor;
import com.zakiis.rpc.processor.server.ServerOnRequestProcessor;
import com.zakiis.rpc.processor.server.ServerOnResponseProcessor;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * The remoting processor
 * <p>Used to encapsulate remote interaction logic.
 * In order to separate the processing business from netty.
 * When netty starts, it will register processors to abstractNettyRemoting#processorTable.</p>
 * Note that when server process client request, you can use sub class {@link ServerOnRequestProcessor}
 * when server need read client response, you can use sub class {@link ServerOnResponseProcessor}
 * when client need read server response, you can use sub class {@link ClientOnResponseProcessor}
 * after using sub class, we can define our business logic by implements {@link TransactionMessageHandler}
 * @date 2023-07-06 10:21:50
 * @author Liu Zhenghua
 */
public interface RemotingProcessor {

	/**
     * Process message
     *
     * @param ctx        Channel handler context.
     * @param rpcMessage rpc message.
     * @throws Exception throws exception process message error.
     */
    void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception;
}
