package com.zakiis.security.jwt.algorithm;

import java.nio.charset.StandardCharsets;

import com.zakiis.security.HMACUtil;
import com.zakiis.security.HMACUtil.HMACType;
import com.zakiis.security.codec.Base64Util;
import com.zakiis.security.jwt.exception.JWTVerificationException;
import com.zakiis.security.jwt.interfaces.DecodedJwt;

public class HMACAlgorithm extends Algorithm {
	
	private String secret;
	private HMACType hmacType;
	
	public HMACAlgorithm(String name, String description, String secret) {
		super(name, description);
		this.secret = secret;
		this.hmacType = HMACType.getByAlogorithm(description);
	}

	@Override
	public byte[] sign(byte[] contentBytes) {
		return HMACUtil.digest(contentBytes, secret.getBytes(), hmacType);
	}

	@Override
	public void verify(DecodedJwt jwt) {
		String signature = jwt.getSignature();
		byte[] contentBytes = String.format("%s.%s", jwt.getHeader(), jwt.getPayload()).getBytes(StandardCharsets.UTF_8);
		String calcSign = Base64Util.encodeToBase64URL(sign(contentBytes));
		if (!signature.equals(calcSign)) {
			throw new JWTVerificationException(String.format("signature verification failed, input signature:%s, calc signature:%s", signature, calcSign));
		}
	}
}
