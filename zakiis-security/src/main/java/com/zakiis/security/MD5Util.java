package com.zakiis.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zakiis.core.error.ZakiisAlgorithmError;
import com.zakiis.security.codec.HexUtil;

/**
 * Message Digest Algorithm
 * @author 10901
 */
public class MD5Util {

	static final String MD5 = "MD5";
	
	public static byte[] digest(byte[] sourceBytes) {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5);
			md.update(sourceBytes);
			byte[] result = md.digest();
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new ZakiisAlgorithmError("No such algorithm", e);
		}
	}
	
	public static String digestAsHex(byte[] sourceBytes) {
		byte[] digest = digest(sourceBytes);
		String result = HexUtil.toHexString(digest);
		return result;
	}
}
