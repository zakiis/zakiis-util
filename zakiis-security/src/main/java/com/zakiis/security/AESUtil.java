package com.zakiis.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.RandomUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.zakiis.core.error.ZakiisAlgorithmError;
import com.zakiis.security.constants.AESMode;
import com.zakiis.security.exception.AESDecryptException;
import com.zakiis.security.exception.AESEncryptException;

/**
 * Advanced Encryption Standard
 * @author 10901
 */
public class AESUtil {
	
	static final String AES = "AES";
	static final int AES_KEY_LENGTH = 256;
	/** initial vector length equals to AES block size 128bit */
	static final int IV_BYTE_LENGTH = 16;
	static final String DEFAULT_AES_MODE = "AES/CBC/PKCS7Padding";
	/** 根据AES的分组规则，IV必须是128bit */
	static final String DEFAULT_IV_SEED = "0000000000000000";
	
	static KeyGenerator kgen;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
		try {
			kgen = KeyGenerator.getInstance(AES);
			// AES256
			kgen.init(AES_KEY_LENGTH, SecureRandom.getInstance("SHA1PRNG"));	
		} catch (NoSuchAlgorithmException e) {
			throw new ZakiisAlgorithmError("No such algorithm", e);
		}
		
	}

	public static byte[] encrypt(byte[] sourceBytes, byte[] keyBytes) { 
		return encrypt(sourceBytes, keyBytes, DEFAULT_IV_SEED.getBytes());
	}
	
	public static byte[] encrypt(byte[] sourceBytes, byte[] keyBytes, byte[] iv) { 
		return encrypt(sourceBytes, keyBytes, iv, DEFAULT_AES_MODE);
	}
	
	/**
	 * encrypt content using AES algorithm
	 * @param sourceBytes content need encrypt
	 * @param keyBytes AES secret key
	 * @param iv initial vector
	 * @param aesMode {@link AESMode}
	 * @return encrypted bytes
	 */
	public static byte[] encrypt(byte[] sourceBytes, byte[] keyBytes, byte[] iv, String aesMode) {
		if (keyBytes == null || keyBytes.length % 8 != 0) {
			throw new IllegalArgumentException("Key length must be one of 16, 24, 32");
		}
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);
		try {
			Cipher cipher = Cipher.getInstance(aesMode);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
			byte[] resultByteArr = cipher.doFinal(sourceBytes);
			return resultByteArr;
		} catch (NoSuchAlgorithmException e) {
			throw new AESEncryptException("No such algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new AESEncryptException("No such padding", e);
		} catch (InvalidKeyException e) {
			throw new AESEncryptException("Invalid key", e);
		} catch (IllegalBlockSizeException e) {
			throw new AESEncryptException("Illegal block size", e);
		} catch (BadPaddingException e) {
			throw new AESEncryptException("Bad padding", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new AESEncryptException("No such algorithm parameter", e);
		}
	}

	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes) {
		return decrypt(encryptedBytes, keyBytes, DEFAULT_IV_SEED.getBytes());
	}
	
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, byte[] iv) {
		return decrypt(encryptedBytes, keyBytes, iv, DEFAULT_AES_MODE);
	}
	
	/**
	 * decrypt content using AES algorithm
	 * @param encryptedBytes content need decrypt
	 * @param keyBytes AES secret key
	 * @param iv initial vector
	 * @param aesMode {@link AESMode}
	 * @return encrypted bytes
	 */
	public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, byte[] iv, String aesMode) {
		if (keyBytes == null || (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32)) {
			throw new IllegalArgumentException("Key length must be in 16, 24, 32");
		}
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);
		try {
			Cipher cipher = Cipher.getInstance(aesMode);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			byte[] resultByteArr = cipher.doFinal(encryptedBytes);
			return resultByteArr;
		} catch (NoSuchAlgorithmException e) {
			throw new AESDecryptException("No such algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new AESDecryptException("No such padding", e);
		} catch (InvalidKeyException e) {
			throw new AESDecryptException("Invalid key", e);
		} catch (IllegalBlockSizeException e) {
			throw new AESDecryptException("Illegal block size", e);
		} catch (BadPaddingException e) {
			throw new AESDecryptException("Bad padding", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new AESDecryptException("No such algorithm parameter", e);
		}
	}

	public static byte[] genKey() {
		byte[] encodedFormat = kgen.generateKey().getEncoded();
		return encodedFormat;
	}
	
	public static byte[] genIV() {
		return RandomUtils.nextBytes(IV_BYTE_LENGTH);
	}
}