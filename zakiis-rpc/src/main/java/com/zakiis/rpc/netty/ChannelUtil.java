package com.zakiis.rpc.netty;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class ChannelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtil.class);
    
    private static final String ENDPOINT_BEGIN_CHAR = "/";
    private static final String IP_PORT_SPLIT_CHAR = ":";

    /**
     * get address from channel
     * @param channel the channel
     * @return address
     */
    public static String getAddressFromChannel(Channel channel) {
        SocketAddress socketAddress = channel.remoteAddress();
        String address = socketAddress.toString();
        if (socketAddress.toString().indexOf(ENDPOINT_BEGIN_CHAR) == 0) {
            address = socketAddress.toString().substring(ENDPOINT_BEGIN_CHAR.length());
        }
        return address;
    }

    /**
     * get client ip from channel
     * @param channel the channel
     * @return client ip
     */
    public static String getClientIpFromChannel(Channel channel) {
        String address = getAddressFromChannel(channel);
        String clientIp = address;
        if (clientIp.contains(IP_PORT_SPLIT_CHAR)) {
            clientIp = clientIp.substring(0, clientIp.lastIndexOf(IP_PORT_SPLIT_CHAR));
        }
        return clientIp;
    }

    /**
     * get client port from channel
     * @param channel the channel
     * @return client port
     */
    public static Integer getClientPortFromChannel(Channel channel) {
        String address = getAddressFromChannel(channel);
        Integer port = 0;
        try {
            if (address.contains(IP_PORT_SPLIT_CHAR)) {
                port = Integer.parseInt(address.substring(address.lastIndexOf(IP_PORT_SPLIT_CHAR) + 1));
            }
        } catch (NumberFormatException exx) {
            LOGGER.error(exx.getMessage());
        }
        return port;
    }
}
