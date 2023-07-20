package com.zakiis.common.test;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.zakiis.core.util.NetUtil;

public class NetUtilTest {

	@Test
	public void test() {
		String localIp = NetUtil.getLocalIp();
		System.out.println(localIp);
		
		List<String> localIps = NetUtil.getLocalIps();
		System.out.println(localIps);
	}
}
