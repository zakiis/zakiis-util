package com.zakiis.rpc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.zakiis.rpc.netty.DefaultNettyRemotingServer;

public class RpcTest {

	@Test
	public void testStartNettyServer() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000));
		DefaultNettyRemotingServer remotingServer = new DefaultNettyRemotingServer(threadPoolExecutor);
		remotingServer.init();
	}
}
