package com.zakiis.rpc.processor.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.util.NetUtil;
import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.TransactionMessageHandler;
import com.zakiis.rpc.netty.ChannelManager;
import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.AbstractResultMessage;
import com.zakiis.rpc.protocol.MessageFuture;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.ChannelHandlerContext;

public class ServerOnResponseProcessor implements RemotingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerOnRequestProcessor.class);

    /**
     * To handle the received RPC message on upper level.
     */
    private TransactionMessageHandler transactionMessageHandler;

    /**
     * The Futures from io.seata.core.rpc.netty.AbstractNettyRemoting#futures
     */
    private ConcurrentMap<Integer, MessageFuture> futures;

    public ServerOnResponseProcessor(TransactionMessageHandler transactionMessageHandler,
                                     ConcurrentHashMap<Integer, MessageFuture> futures) {
        this.transactionMessageHandler = transactionMessageHandler;
        this.futures = futures;
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        MessageFuture messageFuture = futures.remove(rpcMessage.getId());
        if (messageFuture != null) {
            messageFuture.setResultMessage(rpcMessage.getBody());
        } else {
            if (ChannelManager.isRegistered(ctx.channel())) {
                onResponseMessage(ctx, rpcMessage);
            } else {
                try {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("closeChannelHandlerContext channel:" + ctx.channel());
                    }
                    ctx.disconnect();
                    ctx.close();
                } catch (Exception exx) {
                    LOGGER.error(exx.getMessage());
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("close a unhandled connection! [%s]", ctx.channel().toString()));
                }
            }
        }
    }

    private void onResponseMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("server received:{},clientIp:{},vgroup:{}", rpcMessage.getBody(),
                NetUtil.toIpAddress(ctx.channel().remoteAddress()),
                ChannelManager.getContextFromIdentified(ctx.channel()).getTransactionServiceGroup());
        }
        if (rpcMessage.getBody() instanceof AbstractResultMessage) {
            RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
            transactionMessageHandler.onResponse((AbstractResultMessage) rpcMessage.getBody(), rpcContext);
        }
    }
}