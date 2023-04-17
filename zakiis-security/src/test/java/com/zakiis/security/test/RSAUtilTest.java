package com.zakiis.security.test;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;

import org.junit.jupiter.api.Test;

import com.zakiis.security.RSAUtil;
import com.zakiis.security.codec.Base64Util;

public class RSAUtilTest {
	
	@Test
	public void testExtractPubKeyFromPEMEncodedCert() {
		byte[] certBytes = Base64Util.decode("MIID3TCCAsWgAwIBAgIJAOztGPrqMRAQMA0GCSqGSIb3DQEBCwUAMIGUMQswCQYD"
				+ "VQQGEwJDTjETMBEGA1UECAwKR3VhbmcgRG9uZzESMBAGA1UEBwwJU2hlbiBaaGVu"
				+ "MRQwEgYDVQQKDAtPbmUgQ29ubmVjdDELMAkGA1UECwwCSVQxOTA3BgNVBAMMMFBp"
				+ "bmcgQW4gT3JnYW5pemF0aW9uIFZhbGlkYXRpb24gQ0EgLSBTSEEyNTYgLSBHMjAe"
				+ "Fw0yMjAxMTQwNjMzMTZaFw0zMjAxMTIwNjMzMTZaMHkxCzAJBgNVBAYTAlBIMQ8w"
				+ "DQYDVQQIDAZNYW5pbGExDzANBgNVBAcMBlRhZ3VpZzEfMB0GA1UECgwWQ0lNQiBC"
				+ "YW5rIFBoaWxpcHBpbmVzLjELMAkGA1UECwwCSVQxGjAYBgNVBAMMESouY2ltYmJh"
				+ "bmsuY29tLnBoMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwflS05OG"
				+ "AN4oiVUO+tLw4huwFrkKYWqIHi3zB429nbiUdMY/d6sfHnmTCEt55Qz0kNAZUgct"
				+ "03+MneX9yghq8y08DD40kqsd1ekUtR7tGmvD6YnJ+X9vva1rX4P/JoOkavRRApNS"
				+ "qep1tjt2/QM1UtZJheoU7hFF2HCqfEpV30YYHxbFQRWsvVrYhiIhotA1OnLmFegX"
				+ "007dJufBjgst2B+Jc0DijlvcE3OP98qDVNQpGhxPekHEQD8pROLeX4WWFtDQCN3F"
				+ "01UOcjqTA1FdYncf8HP8Ii83/17sUnL6iJzfWFsR9U186N35jyxLpZ/yUOMzVzJ5"
				+ "SbbPqqrqo3zdDwIDAQABo0wwSjALBgNVHQ8EBAMCBeAwHQYDVR0lBBYwFAYIKwYB"
				+ "BQUHAwEGCCsGAQUFBwMCMBwGA1UdEQQVMBOCESouY2ltYmJhbmsuY29tLnBoMA0G"
				+ "CSqGSIb3DQEBCwUAA4IBAQBqNT97HVkOb4zQHCifG6vIP8V3PdMGKuF2ulqeSKBQ"
				+ "Tz/FFv1xljIf9kz6ASoCCDTxBwczu6ul/02ckltQ0uQDlNU9zOS8zQUZB4cipx1+"
				+ "/Gsuw5ag/g1qLzOjenDDmwi5HBF/LhhYOSyQpI1M4oXune6MSpxE2X1NbBBagDx5"
				+ "tJPDOHPmtKsCAtDcr/B+yeANts7sxvgZYgrXJHdECMRj5AYQwaDa7PTPtUrMx48V"
				+ "Cb9ftmyyf6EupkhBY8qTeEJM8hyb3dqALTgh92A2/OFYUEu9VDk25NlFP6n7pmXs"
				+ "dA/XKUGV14Nwey52pNomtHHwgYpsYhGoPmI85qHFqnsu");
		byte[] publicKeyBytes = RSAUtil.extractPubKeyFromPEMEncodedCert(certBytes);
		System.out.println(RSAUtil.formatPubKeyToPEMEncoded(publicKeyBytes));
	}
	
	@Test
	public void test() throws UnsupportedEncodingException {
		String encoding = "UTF-8";
		KeyPair keyPair = RSAUtil.genKeyPair();
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		System.out.println("private key:\n" + RSAUtil.formatPrivateKeyToPEMEncoded(privateKeyBytes));
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		System.out.println("public key:\n" + RSAUtil.formatPubKeyToPEMEncoded(publicKeyBytes));
		
		byte[] encryptedBytes = RSAUtil.encryptByPublicKey("Today is a sunny day.\n今天是一个好天气".getBytes(encoding), publicKeyBytes);
		System.out.println("公钥加密后的内容：" + Base64Util.encode(encryptedBytes));
		
		byte[] decryptBytes = RSAUtil.decryptByPrivateKey(encryptedBytes, privateKeyBytes);
		System.out.println("私钥解密后的内容：" + new String(decryptBytes, encoding));
		
		encryptedBytes = RSAUtil.encryptByPrivateKey("Today is a sunny day.\n今天是一个好天气".getBytes(encoding), privateKeyBytes);
		System.out.println("私钥加密后的内容：" + Base64Util.encode(encryptedBytes));
		
		decryptBytes = RSAUtil.decryptByPublicKey(encryptedBytes, publicKeyBytes);
		System.out.println("公钥解密后的内容：" + new String(decryptBytes, encoding));
	}

}
