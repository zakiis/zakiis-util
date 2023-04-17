package com.zakiis.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.zakiis.core.error.ZakiisAlgorithmError;
import com.zakiis.security.codec.HexUtil;

/**
 * Secured Hash Algorithm
 * @author 10901
 */
public class SHAUtil {
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static byte[] digest(byte[] sourceBytes, SHAType shaType) {
		try {
			MessageDigest md = MessageDigest.getInstance(shaType.getAlgorithm());
			md.update(sourceBytes);
			byte[] result = md.digest();
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new ZakiisAlgorithmError("No such algorithm", e);
		}
	}
	
	public static String digestAsHex(byte[] sourceBytes, SHAType shaType) {
		byte[] digest = digest(sourceBytes, shaType);
		String result = HexUtil.toHexString(digest);
		return result;
	}
	
	public static enum SHAType {
		/** SHA1*/
		SHA1("SHA-1"),
		//SHA2
		SHA_224("SHA-224"),
		SHA_256("SHA-256"),
		SHA_384("SHA-384"),
		SHA_512("SHA-512"),
		//SHA3
		SHA3_224("SHA3-224"),
		SHA3_256("SHA3-256"),
		SHA3_384("SHA3-384"),
		SHA3_512("SHA3-512"),
		;
		String algorithm;
		
		public String getAlgorithm() {
			return algorithm;
		}
		
		private SHAType(String algorithm) {
			this.algorithm = algorithm;
		}
	}
}
