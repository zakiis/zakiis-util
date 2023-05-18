package com.zakiis.security.test;

import org.junit.jupiter.api.Test;

import com.zakiis.security.aes.AesGcmUtil;

public class AesGcmUtilTest {

	@Test
	public void testDecrypt() {
		String mainPassword = "hT8ekQlN0c+rEmUIjytNjQ==";
		String encryptWorkPassword = "xeGIf1I/j6Ltwx8zPZSgWSmwh1QLj+s1j584bQP1vZSEF8RkOizhuLrPahGeo1bA0Bqu3g==";
		String workPassword = AesGcmUtil.decryptFromBase64(encryptWorkPassword, mainPassword);
		System.out.println(String.format("工作密钥:%s\n  解密后为:%s", encryptWorkPassword, workPassword));
		String encryptMsgInBase64 = "tuaA8o2+XIqJhG7mLUm9NTHTZjA0WVh5OdvbQDq19oB31YCQ";
		String msg = AesGcmUtil.decryptFromBase64(encryptMsgInBase64, workPassword);
		System.out.println(String.format("密文:%s\n  解密后的密文为:%s", encryptMsgInBase64, msg));
	}
	
	@Test
	public void testEncrypt() {
		String mainPassword = "hT8ekQlN0c+rEmUIjytNjQ==";
		String workPassword = "1B70MJsVkz3a/lbS8y2EHg==";
		String encryptWorkPassword = AesGcmUtil.encryptToBase64(workPassword, mainPassword);
		System.out.println(String.format("工作密钥:%s\n  加密后的密文为:%s", workPassword, encryptWorkPassword));
		String msg = "Aa654321";
		String encryptMsg = AesGcmUtil.encryptToBase64(msg, workPassword);
		System.out.println(String.format("明文:%s\n  加密后的密文为:%s", msg, encryptMsg));
	}
}
