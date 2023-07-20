package com.zakiis.rpc.processor.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.util.NetUtil;
import com.zakiis.rpc.Disposable;
import com.zakiis.rpc.RemotingServer;
import com.zakiis.rpc.RpcContext;
import com.zakiis.rpc.TransactionMessageHandler;
import com.zakiis.rpc.config.ConfigurationFactory;
import com.zakiis.rpc.config.ConfigurationKeys;
import com.zakiis.rpc.netty.ChannelManager;
import com.zakiis.rpc.netty.NettyServerConfig;
import com.zakiis.rpc.netty.thread.NamedThreadFactory;
import com.zakiis.rpc.processor.RemotingProcessor;
import com.zakiis.rpc.protocol.AbstractMessage;
import com.zakiis.rpc.protocol.AbstractResultMessage;
import com.zakiis.rpc.protocol.BatchResultMessage;
import com.zakiis.rpc.protocol.MergeResultMessage;
import com.zakiis.rpc.protocol.MergedWrapMessage;
import com.zakiis.rpc.protocol.RpcMessage;
import com.zakiis.rpc.protocol.Version;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Server processor logic (server would write response for the client request)
 * request flow: client -> request
 * @date 2023-07-17 16:45:10
 * @author Liu Zhenghua
 */
public class ServerOnRequestProcessor implements RemotingProcessor, Disposable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerOnRequestProcessor.class);

    private final RemotingServer remotingServer;

    private final TransactionMessageHandler transactionMessageHandler;

    private ExecutorService batchResponseExecutorService;

    private final ConcurrentMap<Channel, BlockingQueue<QueueItem>> basketMap = new ConcurrentHashMap<>();
    protected final Object batchResponseLock = new Object();
    private volatile boolean isResponding = false;
    private static final int MAX_BATCH_RESPONSE_MILLS = 1;
    private static final int MAX_BATCH_RESPONSE_THREAD = 1;
    private static final long KEEP_ALIVE_TIME = Integer.MAX_VALUE;
    private static final String BATCH_RESPONSE_THREAD_PREFIX = "rpcBatchResponse";
    private static final boolean PARALLEL_REQUEST_HANDLE =
        ConfigurationFactory.getInstance().getBoolean(ConfigurationKeys.ENABLE_PARALLEL_REQUEST_HANDLE_KEY, true);

    public ServerOnRequestProcessor(RemotingServer remotingServer, TransactionMessageHandler transactionMessageHandler) {
        this.remotingServer = remotingServer;
        this.transactionMessageHandler = transactionMessageHandler;
        if (NettyServerConfig.isEnableTcServerBatchSendResponse()) {
            batchResponseExecutorService = new ThreadPoolExecutor(MAX_BATCH_RESPONSE_THREAD,
                MAX_BATCH_RESPONSE_THREAD,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory(BATCH_RESPONSE_THREAD_PREFIX, MAX_BATCH_RESPONSE_THREAD));
            batchResponseExecutorService.submit(new BatchResponseRunnable());
        }
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        if (ChannelManager.isRegistered(ctx.channel())) {
            onRequestMessage(ctx, rpcMessage);
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

    @Override
    public void destroy() {
        if (batchResponseExecutorService != null) {
            batchResponseExecutorService.shutdown();
        }
    }

    private void onRequestMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        Object message = rpcMessage.getBody();
        RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("server received:{},clientIp:{},vgroup:{}", message,
                NetUtil.toIpAddress(ctx.channel().remoteAddress()), rpcContext.getTransactionServiceGroup());
        }
        if (!(message instanceof AbstractMessage)) {
            return;
        }
        // the batch send request message
        if (message instanceof MergedWrapMessage) {
            if (NettyServerConfig.isEnableTcServerBatchSendResponse() && StringUtils.isNotBlank(rpcContext.getVersion())
                && Version.isAboveOrEqualVersion150(rpcContext.getVersion())) {
                List<AbstractMessage> msgs = ((MergedWrapMessage)message).msgs;
                List<Integer> msgIds = ((MergedWrapMessage)message).msgIds;
                for (int i = 0; i < msgs.size(); i++) {
                    AbstractMessage msg = msgs.get(i);
                    int msgId = msgIds.get(i);
                    if (PARALLEL_REQUEST_HANDLE) {
                        CompletableFuture.runAsync(
                            () -> handleRequestsByMergedWrapMessageBy150(msg, msgId, rpcMessage, ctx, rpcContext));
                    } else {
                        handleRequestsByMergedWrapMessageBy150(msg, msgId, rpcMessage, ctx, rpcContext);
                    }
                }
            } else {
                List<AbstractResultMessage> results = new ArrayList<>();
                List<CompletableFuture<AbstractResultMessage>> completableFutures = null;
                for (int i = 0; i < ((MergedWrapMessage)message).msgs.size(); i++) {
                    if (PARALLEL_REQUEST_HANDLE) {
                        if (completableFutures == null) {
                            completableFutures = new ArrayList<>();
                        }
                        int finalI = i;
                        completableFutures.add(CompletableFuture.supplyAsync(() -> handleRequestsByMergedWrapMessage(
                            ((MergedWrapMessage)message).msgs.get(finalI), rpcContext)));
                    } else {
                        results.add(i,
                            handleRequestsByMergedWrapMessage(((MergedWrapMessage)message).msgs.get(i), rpcContext));
                    }
                }
                if (CollectionUtils.isNotEmpty(completableFutures)) {
                    try {
                        for (CompletableFuture<AbstractResultMessage> completableFuture : completableFutures) {
                            results.add(completableFuture.get());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("handle request error: {}", e.getMessage(), e);
                    }
                }
                MergeResultMessage resultMessage = new MergeResultMessage();
                resultMessage.setMsgs(results.toArray(new AbstractResultMessage[0]));
                remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), resultMessage);
            }
        } else {
            // the single send request message
            final AbstractMessage msg = (AbstractMessage) message;
            AbstractResultMessage result = transactionMessageHandler.onRequest(msg, rpcContext);
            remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), result);
        }
    }

    private void notifyBatchRespondingThread() {
        if (!isResponding) {
            synchronized (batchResponseLock) {
                batchResponseLock.notifyAll();
            }
        }
    }

    private BlockingQueue<QueueItem> computeIfAbsentMsgQueue(Channel channel) {
        return basketMap.computeIfAbsent(channel, key -> new LinkedBlockingQueue<>());
    }

    private void offerMsg(BlockingQueue<QueueItem> msgQueue, RpcMessage rpcMessage,
                          AbstractResultMessage resultMessage, int msgId, Channel channel) {
        if (!msgQueue.offer(new QueueItem(resultMessage, msgId, rpcMessage))) {
            LOGGER.error("put message into basketMap offer failed, channel:{},rpcMessage:{},resultMessage:{}",
                channel, rpcMessage, resultMessage);
        }
    }

    /**
     * batch response runnable
     *
     * @since 1.5.0
     */
    private class BatchResponseRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (batchResponseLock) {
                    try {
                        batchResponseLock.wait(MAX_BATCH_RESPONSE_MILLS);
                    } catch (InterruptedException e) {
                        LOGGER.error("BatchResponseRunnable Interrupted error", e);
                    }
                }
                isResponding = true;
                basketMap.forEach((channel, msgQueue) -> {
                    if (msgQueue.isEmpty()) {
                        return;
                    }
                    // Because the [serialization,compressor,rpcMessageId,headMap] of the response
                    // needs to be the same as the [serialization,compressor,rpcMessageId,headMap] of the request.
                    // Assemble by grouping according to the [serialization,compressor,rpcMessageId,headMap] dimensions.
                    Map<ClientRequestRpcInfo, BatchResultMessage> batchResultMessageMap = new HashMap<>();
                    while (!msgQueue.isEmpty()) {
                        QueueItem item = msgQueue.poll();
                        BatchResultMessage batchResultMessage = batchResultMessageMap.computeIfAbsent(
                            new ClientRequestRpcInfo(item.getRpcMessage()),
                            key -> new BatchResultMessage());
                        batchResultMessage.getResultMessages().add(item.getResultMessage());
                        batchResultMessage.getMsgIds().add(item.getMsgId());
                    }
                    batchResultMessageMap.forEach((clientRequestRpcInfo, batchResultMessage) ->
                        remotingServer.sendAsyncResponse(buildRpcMessage(clientRequestRpcInfo),
                            channel, batchResultMessage));
                });
                isResponding = false;
            }
        }
    }

    /**
     * handle rpc request message
     * @param rpcContext rpcContext
     */
    private AbstractResultMessage handleRequestsByMergedWrapMessage(AbstractMessage subMessage, RpcContext rpcContext) {
        return transactionMessageHandler.onRequest(subMessage, rpcContext);
    }

    /**
     * handle rpc request message
     * @param msg msg
     * @param msgId msgId
     * @param rpcMessage rpcMessage
     * @param ctx ctx
     * @param rpcContext rpcContext
     */
    private void handleRequestsByMergedWrapMessageBy150(AbstractMessage msg, int msgId, RpcMessage rpcMessage,
        ChannelHandlerContext ctx, RpcContext rpcContext) {
        AbstractResultMessage resultMessage = transactionMessageHandler.onRequest(msg, rpcContext);
        BlockingQueue<QueueItem> msgQueue = computeIfAbsentMsgQueue(ctx.channel());
        offerMsg(msgQueue, rpcMessage, resultMessage, msgId, ctx.channel());
        notifyBatchRespondingThread();
    }

    /**
     * build RpcMessage
     *
     * @param clientRequestRpcInfo For saving client request rpc info
     * @return rpcMessage
     */
    private RpcMessage buildRpcMessage(ClientRequestRpcInfo clientRequestRpcInfo) {
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setId(clientRequestRpcInfo.getRpcMessageId());
        rpcMessage.setCodec(clientRequestRpcInfo.getCodec());
        rpcMessage.setCompressor(clientRequestRpcInfo.getCompressor());
        rpcMessage.setHeadMap(clientRequestRpcInfo.getHeadMap());
        return rpcMessage;
    }

    /**
     * For saving client request rpc info
     * <p>
     * Because the [serialization,compressor,rpcMessageId,headMap] of the response
     * needs to be the same as the [serialization,compressor,rpcMessageId,headMap] of the request.
     * Assemble by grouping according to the [serialization,compressor,rpcMessageId,headMap] dimensions.
     */
    private static class ClientRequestRpcInfo {

        /**
         * the Outer layer rpcMessage id
         */
        private int rpcMessageId;

        /**
         * the Outer layer rpcMessage client send request message codec
         */
        private byte codec;

        /**
         * the Outer layer rpcMessage client send request message compressor
         */
        private byte compressor;

        /**
         * the Outer layer rpcMessage headMap
         */
        private Map<String, String> headMap;

        public ClientRequestRpcInfo(RpcMessage rpcMessage) {
            this.rpcMessageId = rpcMessage.getId();
            this.codec = rpcMessage.getCodec();
            this.compressor = rpcMessage.getCompressor();
            this.headMap = rpcMessage.getHeadMap();
        }

        public int getRpcMessageId() {
            return rpcMessageId;
        }

        public byte getCodec() {
            return codec;
        }

        public byte getCompressor() {
            return compressor;
        }

        public Map<String, String> getHeadMap() {
            return headMap;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ClientRequestRpcInfo that = (ClientRequestRpcInfo) o;
            return rpcMessageId == that.rpcMessageId && codec == that.codec
                && compressor == that.compressor && headMap.equals(that.headMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rpcMessageId, codec, compressor, headMap);
        }
    }

    /**
     * the queue item
     *
     * @see ServerOnRequestProcessor#basketMap
     */
    private static class QueueItem {

        /**
         * the result message
         */
        private AbstractResultMessage resultMessage;

        /**
         * result message id
         */
        private Integer msgId;

        /**
         * the Outer layer rpcMessage
         */
        private RpcMessage rpcMessage;

        public QueueItem(AbstractResultMessage resultMessage, int msgId, RpcMessage rpcMessage) {
            this.resultMessage = resultMessage;
            this.msgId = msgId;
            this.rpcMessage = rpcMessage;
        }

        public AbstractResultMessage getResultMessage() {
            return resultMessage;
        }

        public Integer getMsgId() {
            return msgId;
        }

        public RpcMessage getRpcMessage() {
            return rpcMessage;
        }
    }

}
