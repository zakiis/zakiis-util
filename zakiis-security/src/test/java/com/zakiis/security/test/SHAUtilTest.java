package com.zakiis.security.test;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import com.zakiis.security.SHAUtil;

public class SHAUtilTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		String encoding = "UTF-8";
		String digest = SHAUtil.digestAsHex("Today is a sunny day.\n今天是一个好天气".getBytes(encoding), SHAUtil.SHAType.SHA3_512);
		System.out.println(digest);
	}
}
