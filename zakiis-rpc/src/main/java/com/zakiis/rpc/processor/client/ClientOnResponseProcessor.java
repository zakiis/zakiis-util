package com.zakiis.rpc.processor.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.rpc.TransactionMessageHandler;
import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.AbstractResultMessage;
import com.zakiis.rpc.protocol.BatchResultMessage;
import com.zakiis.rpc.protocol.MergeMessage;
import com.zakiis.rpc.protocol.MergeResultMessage;
import com.zakiis.rpc.protocol.MergedWrapMessage;
import com.zakiis.rpc.protocol.MessageFuture;
import com.zakiis.rpc.protocol.RpcMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientOnResponseProcessor implements RemotingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientOnResponseProcessor.class);

    /**
     * The Merge msg map from io.seata.core.rpc.netty.AbstractNettyRemotingClient#mergeMsgMap.
     */
    private Map<Integer, MergeMessage> mergeMsgMap;

    /**
     * The Futures from io.seata.core.rpc.netty.AbstractNettyRemoting#futures
     */
    private final ConcurrentMap<Integer, MessageFuture> futures;

    /**
     * To handle the received RPC message on upper level.
     */
    private final TransactionMessageHandler transactionMessageHandler;

    public ClientOnResponseProcessor(Map<Integer, MergeMessage> mergeMsgMap,
                                     ConcurrentHashMap<Integer, MessageFuture> futures,
                                     TransactionMessageHandler transactionMessageHandler) {
        this.mergeMsgMap = mergeMsgMap;
        this.futures = futures;
        this.transactionMessageHandler = transactionMessageHandler;
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        if (rpcMessage.getBody() instanceof MergeResultMessage) {
            MergeResultMessage results = (MergeResultMessage) rpcMessage.getBody();
            MergedWrapMessage mergeMessage = (MergedWrapMessage) mergeMsgMap.remove(rpcMessage.getId());
            for (int i = 0; i < mergeMessage.msgs.size(); i++) {
                int msgId = mergeMessage.msgIds.get(i);
                MessageFuture future = futures.remove(msgId);
                if (future == null) {
                    LOGGER.error("msg: {} is not found in futures, result message: {}", msgId,results.getMsgs()[i]);
                } else {
                    future.setResultMessage(results.getMsgs()[i]);
                }
            }
        } else if (rpcMessage.getBody() instanceof BatchResultMessage) {
            try {
                BatchResultMessage batchResultMessage = (BatchResultMessage) rpcMessage.getBody();
                for (int i = 0; i < batchResultMessage.getMsgIds().size(); i++) {
                    int msgId = batchResultMessage.getMsgIds().get(i);
                    MessageFuture future = futures.remove(msgId);
                    if (future == null) {
                        LOGGER.error("msg: {} is not found in futures, result message: {}", msgId, batchResultMessage.getResultMessages().get(i));
                    } else {
                        future.setResultMessage(batchResultMessage.getResultMessages().get(i));
                    }
                }
            } finally {
                // In order to be compatible with the old version, in the batch sending of version 1.5.0,
                // batch messages will also be placed in the local cache of mergeMsgMap,
                // but version 1.5.0 no longer needs to obtain batch messages from mergeMsgMap
                mergeMsgMap.clear();
            }
        } else {
            MessageFuture messageFuture = futures.remove(rpcMessage.getId());
            if (messageFuture != null) {
                messageFuture.setResultMessage(rpcMessage.getBody());
            } else {
                if (rpcMessage.getBody() instanceof AbstractResultMessage) {
                    if (transactionMessageHandler != null) {
                        transactionMessageHandler.onResponse((AbstractResultMessage) rpcMessage.getBody(), null);
                    }
                }
            }
        }
    }
}