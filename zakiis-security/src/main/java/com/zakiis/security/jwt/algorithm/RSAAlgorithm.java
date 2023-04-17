package com.zakiis.security.jwt.algorithm;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

import com.zakiis.security.RSAUtil;
import com.zakiis.security.SHAUtil;
import com.zakiis.security.SHAUtil.SHAType;
import com.zakiis.security.codec.Base64Util;
import com.zakiis.security.exception.RSADecryptException;
import com.zakiis.security.jwt.exception.JWTVerificationException;
import com.zakiis.security.jwt.interfaces.DecodedJwt;
import com.zakiis.security.jwt.interfaces.RSAKeyProvider;

public class RSAAlgorithm extends Algorithm {
	
	 private final RSAKeyProvider keyProvider;
	 private final SHAType shaType;

	protected RSAAlgorithm(String name, String description, RSAKeyProvider keyProvider) {
		super(name, description);
		if (keyProvider == null) {
            throw new IllegalArgumentException("The Key Provider cannot be null.");
        }
		//SHA256withRSA
		String[] split = description.split("with");
		shaType = SHAType.valueOf(split[0].substring(0, 3) + "_" + split[0].substring(3));
		this.keyProvider = keyProvider;
	}

	@Override
	public byte[] sign(byte[] contentBytes) {
		keyProvider.getPrivateKey();
		byte[] digest = SHAUtil.digest(contentBytes, shaType);
		byte[] encrypt = RSAUtil.encryptByPrivateKey(digest, keyProvider.getPrivateKey().getEncoded());
		return encrypt;
	}

	@Override
	public void verify(DecodedJwt jwt) {
		String signature = jwt.getSignature();
		try {
			byte[] digest = RSAUtil.decryptByPublicKey(Base64Util.decodeFromBase64URL(signature), keyProvider.getPublicKeyById(jwt.getKeyId()).getEncoded());
			byte[] contentBytes = String.format("%s.%s", jwt.getHeader(), jwt.getPayload()).getBytes(StandardCharsets.UTF_8);
			byte[] calcDigest = SHAUtil.digest(contentBytes, shaType);
			if (!Objects.deepEquals(digest, calcDigest)) {
				throw new JWTVerificationException(String.format("signature verification failed, input signature:%s", signature));
			}
		} catch (RSADecryptException e) {
			throw new JWTVerificationException(String.format("signature decryption failed", e));
		}
	}

	public static RSAKeyProvider providerForKeys(final RSAPublicKey publicKey, final RSAPrivateKey privateKey) {
        if (publicKey == null && privateKey == null) {
            throw new IllegalArgumentException("Both provided Keys cannot be null.");
        }
        return new RSAKeyProvider() {
            @Override
            public RSAPublicKey getPublicKeyById(String keyId) {
                return publicKey;
            }

            @Override
            public RSAPrivateKey getPrivateKey() {
                return privateKey;
            }

            @Override
            public String getPrivateKeyId() {
                return null;
            }
        };
    }
}
