package com.zakiis.security;

import java.io.ByteArrayInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.zakiis.core.error.ZakiisAlgorithmError;
import com.zakiis.security.codec.Base64Util;
import com.zakiis.security.exception.RSADecryptException;
import com.zakiis.security.exception.RSAEncryptException;

public class RSAUtil {

	static final String X509 = "X.509";
	static final Integer PEM_PER_LINE_LENGTH = 64;
	static final String RSA = "RSA";
	static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
	
	static KeyPairGenerator keyPairGen;
	static KeyFactory keyFactory;
	static {
		Security.addProvider(new BouncyCastleProvider());
		try {
			keyPairGen = KeyPairGenerator.getInstance(RSA);
			keyPairGen.initialize(2048);
			keyFactory = KeyFactory.getInstance(RSA);
		} catch (NoSuchAlgorithmException e) {
			throw new ZakiisAlgorithmError("No such algorithm", e);
		}
	}
	
	public static byte[] encryptByPublicKey(byte[] source, byte[] keyBytes) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_MODE);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] result = cipher.doFinal(source);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RSAEncryptException("No such algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new RSAEncryptException("No such padding", e);
		} catch (InvalidKeyException e) {
			throw new RSAEncryptException("Invalid key", e);
		} catch (IllegalBlockSizeException e) {
			throw new RSAEncryptException("Illegal block size", e);
		} catch (BadPaddingException e) {
			throw new RSAEncryptException("Bad padding", e);
		} catch (InvalidKeySpecException e) {
			throw new RSAEncryptException("Illegal key spec", e);
		}
	}
	
	public static byte[] encryptByPrivateKey(byte[] source, byte[] keyBytes) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_MODE);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] result = cipher.doFinal(source);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RSAEncryptException("No such algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new RSAEncryptException("No such padding", e);
		} catch (InvalidKeyException e) {
			throw new RSAEncryptException("Invalid key", e);
		} catch (IllegalBlockSizeException e) {
			throw new RSAEncryptException("Illegal block size", e);
		} catch (BadPaddingException e) {
			throw new RSAEncryptException("Bad padding", e);
		} catch (InvalidKeySpecException e) {
			throw new RSAEncryptException("Illegal key spec", e);
		}
	}
	
	public static byte[] decryptByPrivateKey(byte[] source, byte[] keyBytes) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_MODE);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] result = cipher.doFinal(source);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RSADecryptException("No such algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new RSADecryptException("No such padding", e);
		} catch (InvalidKeyException e) {
			throw new RSADecryptException("Invalid key", e);
		} catch (IllegalBlockSizeException e) {
			throw new RSADecryptException("Illegal block size", e);
		} catch (BadPaddingException e) {
			throw new RSADecryptException("Bad padding", e);
		} catch (InvalidKeySpecException e) {
			throw new RSADecryptException("Illegal key spec", e);
		}
	}
	
	public static byte[] decryptByPublicKey(byte[] source, byte[] keyBytes) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_MODE);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] result = cipher.doFinal(source);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RSADecryptException("No such algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new RSADecryptException("No such padding", e);
		} catch (InvalidKeyException e) {
			throw new RSADecryptException("Invalid key", e);
		} catch (IllegalBlockSizeException e) {
			throw new RSADecryptException("Illegal block size", e);
		} catch (BadPaddingException e) {
			throw new RSADecryptException("Bad padding", e);
		} catch (InvalidKeySpecException e) {
			throw new RSADecryptException("Illegal key spec", e);
		}
	}
	
	public static byte[] extractPubKeyFromPEMEncodedCert(byte[] certPemBytes) {
		try {
			CertificateFactory factory = CertificateFactory.getInstance(X509);
			X509Certificate cert = (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certPemBytes));
			PublicKey publicKey = cert.getPublicKey();
			byte[] result = publicKey.getEncoded();
			return result;	
		} catch(CertificateException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	
	public static String formatPubKeyToPEMEncoded(byte[] pubKeyBytes) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("-----BEGIN PUBLIC KEY-----\n");
		String base64Str = Base64Util.encode(pubKeyBytes);
		int i = 0;
		do {
			int copyLength = Math.min(PEM_PER_LINE_LENGTH, base64Str.length() - i);
			buffer.append(base64Str.substring(i, i + copyLength));
			i += copyLength;
			buffer.append("\n");
		} while (i < base64Str.length());
		buffer.append("-----END PUBLIC KEY-----");
		return buffer.toString();
	}
	
	public static String formatPrivateKeyToPEMEncoded(byte[] pubKeyBytes) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("-----BEGIN RSA PRIVATE KEY-----\n");
		String base64Str = Base64Util.encode(pubKeyBytes);
		int i = 0;
		do {
			int copyLength = Math.min(PEM_PER_LINE_LENGTH, base64Str.length() - i);
			buffer.append(base64Str.substring(i, i + copyLength));
			i += copyLength;
			buffer.append("\n");
		} while (i < base64Str.length());
		buffer.append("-----END RSA PRIVATE KEY-----");
		return buffer.toString();
	}
	
	public static KeyPair genKeyPair() {
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}
}
