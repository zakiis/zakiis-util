package com.zakiis.security.test;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import com.zakiis.security.AESUtil;
import com.zakiis.security.codec.HexUtil;

public class AESUtilTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		String encoding = "UTF-8";
		byte[] keyBytes = AESUtil.genKey();
		System.out.println("AES key:" + HexUtil.toHexString(keyBytes));
		byte[] encryptedBytes = AESUtil.encrypt("Today is a sunny day.\n今天是一个好天气".getBytes(encoding), keyBytes);
		System.out.println("加密后的内容：" + HexUtil.toHexString(encryptedBytes));
		byte[] decryptBytes = AESUtil.decrypt(encryptedBytes, keyBytes);
		System.out.println("解密后的内容：" + new String(decryptBytes, encoding));
	}
}
