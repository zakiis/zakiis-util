package com.zakiis.rpc.netty;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.CollectionUtils;

import com.zakiis.core.util.NetUtil;
import com.zakiis.rpc.DefaultValues;
import com.zakiis.rpc.RemotingBootstrap;
import com.zakiis.rpc.config.ConfigurationFactory;
import com.zakiis.rpc.config.ConfigurationKeys;
import com.zakiis.rpc.discovery.registry.MultiRegistryFactory;
import com.zakiis.rpc.discovery.registry.RegistryService;
import com.zakiis.rpc.netty.codec.ProtocolV1Decoder;
import com.zakiis.rpc.netty.codec.ProtocolV1Encoder;
import com.zakiis.rpc.netty.thread.NamedThreadFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerBootstrap implements RemotingBootstrap {

	private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    private final EventLoopGroup eventLoopGroupWorker;
    private final EventLoopGroup eventLoopGroupBoss;
    private final NettyServerConfig nettyServerConfig;
    private ChannelHandler[] channelHandlers;
    private int listenPort;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public NettyServerBootstrap(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
        if (NettyServerConfig.enableEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(nettyServerConfig.getBossThreadSize(),
                new NamedThreadFactory(nettyServerConfig.getBossThreadPrefix(), nettyServerConfig.getBossThreadSize()));
            this.eventLoopGroupWorker = new EpollEventLoopGroup(nettyServerConfig.getServerWorkerThreads(),
                new NamedThreadFactory(nettyServerConfig.getWorkerThreadPrefix(),
                    nettyServerConfig.getServerWorkerThreads()));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(nettyServerConfig.getBossThreadSize(),
                new NamedThreadFactory(nettyServerConfig.getBossThreadPrefix(), nettyServerConfig.getBossThreadSize()));
            this.eventLoopGroupWorker = new NioEventLoopGroup(nettyServerConfig.getServerWorkerThreads(),
                new NamedThreadFactory(nettyServerConfig.getWorkerThreadPrefix(),
                    nettyServerConfig.getServerWorkerThreads()));
        }
    }

    /**
     * Sets channel handlers.
     *
     * @param handlers the handlers
     */
    protected void setChannelHandlers(final ChannelHandler... handlers) {
        if (handlers != null) {
            channelHandlers = handlers;
        }
    }

    /**
     * Add channel pipeline last.
     *
     * @param channel  the channel
     * @param handlers the handlers
     */
    private void addChannelPipelineLast(Channel channel, ChannelHandler... handlers) {
        if (channel != null && handlers != null) {
            channel.pipeline().addLast(handlers);
        }
    }

    /**
     * use for mock
     *
     * @param listenPort the listen port
     */
    public void setListenPort(int listenPort) {
        if (listenPort <= 0) {
            throw new IllegalArgumentException("listen port: " + listenPort + " is invalid!");
        }
        this.listenPort = listenPort;
    }

    /**
     * Gets listen port.
     *
     * @return the listen port
     */
    public int getListenPort() {
        if (listenPort != 0) {
            return listenPort;
        }
        String strPort = ConfigurationFactory.getInstance().getConfig(ConfigurationKeys.SERVER_SERVICE_PORT_CAMEL);
        int port = 0;
        try {
            port = Integer.parseInt(strPort);
        } catch (NumberFormatException exx) {
            log.error("server service port set error:{}", exx.getMessage());
        }
        if (port <= 0) {
            log.error("listen port: {} is invalid, will use default port:{}", port, DefaultValues.SERVICE_DEFAULT_PORT);
            port = DefaultValues.SERVICE_DEFAULT_PORT;
        }
        listenPort = port;
        return port;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void start() {
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupWorker)
            .channel(NettyServerConfig.SERVER_CHANNEL_CLAZZ)
            .option(ChannelOption.SO_BACKLOG, nettyServerConfig.getSoBackLogSize())
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSendBufSize())
            .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketResvBufSize())
            .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                new WriteBufferWaterMark(nettyServerConfig.getWriteBufferLowWaterMark(),
                    nettyServerConfig.getWriteBufferHighWaterMark()))
            .localAddress(new InetSocketAddress(getListenPort()))
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new IdleStateHandler(NettyServerConfig.MAX_READ_IDLE_SECONDS, 0, 0))
                        .addLast(new ProtocolV1Decoder())
                        .addLast(new ProtocolV1Encoder());
                    if (channelHandlers != null) {
                        addChannelPipelineLast(ch, channelHandlers);
                    }

                }
            });

        try {
            this.serverBootstrap.bind(getListenPort()).sync();
            log.info("Server started, service listen port: {}", getListenPort());
            List<String> localIps = NetUtil.getLocalIps();
            if (CollectionUtils.isEmpty(localIps)) {
            	throw new RuntimeException("no local ip found");
            }
            String ip = localIps.get(0);
            if (localIps.size() > 0) {
            	log.info("found ips {}, choose the first ip {} to register", localIps, ip);
            }
            InetSocketAddress address = new InetSocketAddress(ip, getListenPort());
            for (RegistryService registryService : MultiRegistryFactory.getInstances()) {
                registryService.register(address);
            }
            initialized.set(true);
        } catch (SocketException se) {
            throw new RuntimeException("Server start failed, the listen port: " + getListenPort(), se);
        } catch (Exception exx) {
            throw new RuntimeException("Server start failed", exx);
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void shutdown() {
        try {
            if (log.isInfoEnabled()) {
                log.info("Shutting server down, the listen port: {}", getListenPort());
            }
            if (initialized.get()) {
            	List<String> localIps = NetUtil.getLocalIps();
                if (CollectionUtils.isEmpty(localIps)) {
                	throw new RuntimeException("no local ip found");
                }
            	String ip = localIps.get(0);
                InetSocketAddress address = new InetSocketAddress(ip, getListenPort());
                for (RegistryService registryService : MultiRegistryFactory.getInstances()) {
                    registryService.unregister(address);
                    registryService.close();
                }
                //wait a few seconds for server transport
                TimeUnit.SECONDS.sleep(nettyServerConfig.getServerShutdownWaitTime());
            }

            this.eventLoopGroupBoss.shutdownGracefully();
            this.eventLoopGroupWorker.shutdownGracefully();
        } catch (Exception exx) {
            log.error("shutdown execute error: {}", exx.getMessage(), exx);
        }
    }
}
