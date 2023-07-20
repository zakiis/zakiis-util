package com.zakiis.rpc.netty;

import java.net.InetSocketAddress;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.util.NetUtil;
import com.zakiis.rpc.exception.FrameworkException;
import com.zakiis.rpc.protocol.RegisterRMResponse;
import com.zakiis.rpc.protocol.RegisterTMResponse;

import io.netty.channel.Channel;

public class NettyPoolableFactory implements KeyedPooledObjectFactory<NettyPoolKey, Channel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyPoolableFactory.class);

    private final AbstractNettyRemotingClient rpcRemotingClient;

    private final NettyClientBootstrap clientBootstrap;

    /**
     * Instantiates a new Netty key poolable factory.
     *
     * @param rpcRemotingClient the rpc remoting client
     */
    public NettyPoolableFactory(AbstractNettyRemotingClient rpcRemotingClient, NettyClientBootstrap clientBootstrap) {
        this.rpcRemotingClient = rpcRemotingClient;
        this.clientBootstrap = clientBootstrap;
    }

    @Override
    public PooledObject<Channel> makeObject(NettyPoolKey key) {
        InetSocketAddress address = NetUtil.toInetSocketAddress(key.getAddress());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("NettyPool create channel to " + key);
        }
        Channel tmpChannel = clientBootstrap.getNewChannel(address);
        long start = System.currentTimeMillis();
        Object response;
        Channel channelToServer = null;
        if (key.getMessage() == null) {
            throw new FrameworkException("register msg is null, role:" + key.getTransactionRole().name());
        }
        try {
            response = rpcRemotingClient.sendSyncRequest(tmpChannel, key.getMessage());
            if (!isRegisterSuccess(response, key.getTransactionRole())) {
                rpcRemotingClient.onRegisterMsgFail(key.getAddress(), tmpChannel, response, key.getMessage());
            } else {
                channelToServer = tmpChannel;
                rpcRemotingClient.onRegisterMsgSuccess(key.getAddress(), tmpChannel, response, key.getMessage());
            }
        } catch (Exception exx) {
            if (tmpChannel != null) {
                tmpChannel.close();
            }
            throw new FrameworkException(
                "register " + key.getTransactionRole().name() + " error, errMsg:" + exx.getMessage());
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("register success, cost " + (System.currentTimeMillis() - start) + " ms, version:" + getVersion(
                response, key.getTransactionRole()) + ",role:" + key.getTransactionRole().name() + ",channel:"
                + channelToServer);
        }
        return new DefaultPooledObject<Channel>(channelToServer);
    }

    private boolean isRegisterSuccess(Object response, NettyPoolKey.TransactionRole transactionRole) {
        if (response == null) {
            return false;
        }
        if (transactionRole.equals(NettyPoolKey.TransactionRole.TMROLE)) {
            if (!(response instanceof RegisterTMResponse)) {
                return false;
            }
            RegisterTMResponse registerTMResponse = (RegisterTMResponse)response;
            return registerTMResponse.isIdentified();
        } else if (transactionRole.equals(NettyPoolKey.TransactionRole.RMROLE)) {
            if (!(response instanceof RegisterRMResponse)) {
                return false;
            }
            RegisterRMResponse registerRMResponse = (RegisterRMResponse)response;
            return registerRMResponse.isIdentified();
        }
        return false;
    }

    private String getVersion(Object response, NettyPoolKey.TransactionRole transactionRole) {
        if (transactionRole.equals(NettyPoolKey.TransactionRole.TMROLE)) {
            return ((RegisterTMResponse) response).getVersion();
        } else {
            return ((RegisterRMResponse) response).getVersion();
        }
    }

    @Override
    public void destroyObject(NettyPoolKey key, PooledObject<Channel> pooledObject) throws Exception {
    	Channel channel = pooledObject.getObject();
        if (channel != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("will destroy channel:" + channel);
            }
            channel.disconnect();
            channel.close();
        }
    }

    @Override
    public boolean validateObject(NettyPoolKey key, PooledObject<Channel> pooledObject) {
    	Channel channel = pooledObject.getObject();
        if (channel != null && channel.isActive()) {
            return true;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("channel valid false,channel:" + channel);
        }
        return false;
    }

    @Override
    public void activateObject(NettyPoolKey key, PooledObject<Channel> obj) throws Exception {

    }

    @Override
    public void passivateObject(NettyPoolKey key, PooledObject<Channel> obj) throws Exception {

    }
}