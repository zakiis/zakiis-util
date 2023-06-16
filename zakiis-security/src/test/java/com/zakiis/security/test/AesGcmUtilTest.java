package com.zakiis.security.test;

import org.junit.jupiter.api.Test;

import com.zakiis.security.aes.AesGcmUtil;

public class AesGcmUtilTest {

	@Test
	public void testDecrypt() {
		String mainPassword = "p/TfHAcpXxDxscCz2pYSyA==";
		String encryptWorkPassword = "GFqGYfSLV/aRYXG2zvr5OslA2siGHetEGwUKJ7Usllz7nlSqKFeBC2g9S06Lzj5+pEwSXw==";
		String workPassword = AesGcmUtil.decryptFromBase64(encryptWorkPassword, mainPassword);
		System.out.println(String.format("工作密钥:%s\n  解密后为:%s", encryptWorkPassword, workPassword));
		String encryptMsgInBase64 = "QdeGANgPQj4R2YXOfJUKdY+nDDRzdP0XtWlRsgr3qa2mp932";
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
	
	@Test
	public void test() {
		//m3z2Lx59UtYN/DWJgZWIlg==
		System.out.println(AesGcmUtil.decryptFromBase64("GFqGYfSLV/aRYXG2zvr5OslA2siGHetEGwUKJ7Usllz7nlSqKFeBC2g9S06Lzj5+pEwSXw==", "p/TfHAcpXxDxscCz2pYSyA=="));
		System.out.println(AesGcmUtil.decryptFromBase64("K4BdljiLRuSKhvjWISJ5zhvVdgM6VsggbggCanb5u4Cjzg==", "m3z2Lx59UtYN/DWJgZWIlg=="));
		System.out.println(AesGcmUtil.encryptToBase64("-_zlPC_QQwxojzpt7YC55MLxU03eLLW_ytOZa9pg5YE=", "m3z2Lx59UtYN/DWJgZWIlg=="));
	}
}
