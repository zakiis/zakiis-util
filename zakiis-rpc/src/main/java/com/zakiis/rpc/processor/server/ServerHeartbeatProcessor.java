package com.zakiis.rpc.processor.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.rpc.RemotingServer;
import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.HeartbeatMessage;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.ChannelHandlerContext;

public class ServerHeartbeatProcessor implements RemotingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHeartbeatProcessor.class);

    private RemotingServer remotingServer;

    public ServerHeartbeatProcessor(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        try {
            remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), HeartbeatMessage.PONG);
        } catch (Throwable throwable) {
            LOGGER.error("send response error: {}", throwable.getMessage(), throwable);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("received PING from {}", ctx.channel().remoteAddress());
        }
    }

}
