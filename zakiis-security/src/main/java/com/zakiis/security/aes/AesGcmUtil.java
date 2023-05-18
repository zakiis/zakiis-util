package com.zakiis.security.aes;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.SneakyThrows;

public class AesGcmUtil {
	
	private static final String AES = "AES";
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int KEY_ITERATIONS = 1000;
    private static final int KEY_SIZE = 256;
    private static final int KEY_SALT_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final RandomIvGenerator IV_GENERATOR = new RandomIvGenerator();
    
    public static String encryptToBase64(String msg, String password) {
    	byte[] msgBytes = msg.getBytes();
    	char[] passwordChars = password.toCharArray();
    	byte[] encryptBytes = encrypt(msgBytes, passwordChars, null);
    	return Base64.getEncoder().encodeToString(encryptBytes);
    }

	public static String decryptFromBase64(String encryptedMsgInBase64, String password) {
		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMsgInBase64);
		return new String(decrypt(encryptedBytes, password.toCharArray(), null), StandardCharsets.UTF_8);
	}
	
	@SneakyThrows
	public static byte[] encrypt(byte[] msgBytes, char[] password, byte[] salt) {
		SecretKey key = new SecretKeySpec(genKey(password, salt), AES);
		byte[] iv = IV_GENERATOR.generateIv(GCM_IV_LENGTH);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] cipherMsgBytes = cipher.doFinal(msgBytes);
        byte[] cipherBytes = new byte[iv.length + cipherMsgBytes.length];
        System.arraycopy(iv, 0, cipherBytes, 0, iv.length);
        System.arraycopy(cipherMsgBytes, 0, cipherBytes, iv.length, cipherMsgBytes.length);
        return cipherBytes;
    }
	
	@SneakyThrows
	public static byte[] decrypt(byte[] encryptedBytes, char[] password, byte[] salt) {
		SecretKey key = new SecretKeySpec(genKey(password, salt), AES);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, encryptedBytes, 0, GCM_IV_LENGTH);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        return cipher.doFinal(encryptedBytes, GCM_IV_LENGTH, encryptedBytes.length - GCM_IV_LENGTH);
    }
	
	public static byte[] genKey(char[] password) {
		return genKey(password, ZeroSaltGenerator.generateSalt(KEY_SALT_LENGTH));
	}
	
	public static byte[] genKey(String passwordStr, String saltInBase64) {
		char[] password = passwordStr.toCharArray();
		byte[] salt = Base64.getDecoder().decode(saltInBase64);
		return genKey(password, salt);
	}
	
	@SneakyThrows
	public static byte[] genKey(char[] password, byte[] salt) {
		if (salt == null) {
			salt = ZeroSaltGenerator.generateSalt(KEY_SALT_LENGTH);
		}
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		KeySpec keySpec = new PBEKeySpec(password, salt, KEY_ITERATIONS, KEY_SIZE);
		return factory.generateSecret(keySpec).getEncoded();
	}
}
