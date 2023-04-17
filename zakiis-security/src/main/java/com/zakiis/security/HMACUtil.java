package com.zakiis.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.zakiis.core.error.ZakiisAlgorithmError;
import com.zakiis.security.codec.HexUtil;
import com.zakiis.security.exception.DigestException;

/**
 * Hash-based Message Authentication Code 
 * @author 10901
 */
public class HMACUtil {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static byte[] digest(byte[] sourceBytes, byte[] secretBytes, HMACType hmacType) {
		try {
			Mac mac = Mac.getInstance(hmacType.getAlgorithm());
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretBytes, hmacType.getAlgorithm());
			mac.init(secretKeySpec);
			byte[] result = mac.doFinal(sourceBytes);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new DigestException("No such algorithm", e);
		} catch (InvalidKeyException e) {
			throw new DigestException("Invalid key", e);
		}
	}
	
	public static String digestAsHex(byte[] sourceBytes, byte[] secretBytes, HMACType hmacType) {
		byte[] digest = digest(sourceBytes, secretBytes, hmacType);
		String result = HexUtil.toHexString(digest);
		return result;
	}
	
	public static byte[] genSecretKey(HMACType hmacType) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(hmacType.getAlgorithm());
			SecretKey secretKey = generator.generateKey();
			byte[] result = secretKey.getEncoded();
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new ZakiisAlgorithmError("No such algorithm", e);
		}
	}
	
	public static enum HMACType {
		HMAC_MD5("HMAC-MD5"),
		/** SHA1*/
		HMAC_SHA1("HMAC-SHA1"),
		// SHA2
		HMAC_SHA_224("HMAC-SHA224"),
		HMAC_SHA_256("HMAC-SHA256"),
		HMAC_SHA_384("HMAC-SHA384"),
		HMAC_SHA_512("HMAC-SHA512"),
		// SHA3
		HMAC_SHA3_224("HMAC-SHA3-224"),
		HMAC_SHA3_256("HMAC-SHA3-256"),
		HMAC_SHA3_384("HMAC-SHA3-384"),
		HMAC_SHA3_512("HMAC-SHA3-512"),
		;
		private String algorithm;
		
		public String getAlgorithm() {
			return algorithm;
		}
		
		public static HMACType getByAlogorithm(String algorithm) {
			for (HMACType hmacType : values()) {
				if (hmacType.getAlgorithm().equals(algorithm)) {
					return hmacType;
				}
			}
			return null;
		}
		
		private HMACType(String algorithm) {
			this.algorithm = algorithm;
		}
	}
}
