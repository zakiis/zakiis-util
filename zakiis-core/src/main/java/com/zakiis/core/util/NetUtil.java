package com.zakiis.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;

public class NetUtil {

	@SneakyThrows
	public static String getLocalIp() {
		return Inet4Address.getLocalHost().getHostAddress();
	}
	
	@SneakyThrows
	public static List<String> getLocalIps() {
		List<String> localIps = new ArrayList<String>();
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			if (!networkInterface.isUp() || networkInterface.isVirtual() || networkInterface.isLoopback()) {
				continue;
			}
			Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress inetAddress = inetAddresses.nextElement();
				if (inetAddress != null && (inetAddress instanceof Inet4Address)) {
					localIps.add(inetAddress.getHostAddress());
				}
			}
		}
		return localIps;
	}
	
	@SneakyThrows
	public static InetAddress ipToInetAddress(String ip) {
		return InetAddress.getByName(ip);
	}
	
	public static String toStringAddress(SocketAddress address) {
        if (address == null) {
            return StringUtils.EMPTY;
        }
        return toStringAddress((InetSocketAddress) address);
    }
	
	public static String toStringAddress(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
	
	public static InetSocketAddress toInetSocketAddress(String address) {
        int i = address.indexOf(':');
        String host;
        int port;
        if (i > -1) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = 0;
        }
        return new InetSocketAddress(host, port);
    }
	
	public static String toIpAddress(SocketAddress address) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
        return inetSocketAddress.getAddress().getHostAddress();
    }
}
