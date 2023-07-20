package com.zakiis.rpc.processor.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.HeartbeatMessage;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientHeartbeatProcessor implements RemotingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHeartbeatProcessor.class);

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        if (rpcMessage.getBody() == HeartbeatMessage.PONG) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("received PONG from {}", ctx.channel().remoteAddress());
            }
        }
    }
}

