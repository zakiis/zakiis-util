package com.zakiis.security.test;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.security.RSAUtil;
import com.zakiis.security.jwt.JWTUtil;
import com.zakiis.security.jwt.algorithm.Algorithm;
import com.zakiis.security.jwt.algorithm.RSAAlgorithm;
import com.zakiis.security.jwt.exception.JWTVerificationException;
import com.zakiis.security.jwt.interfaces.DecodedJwt;
import com.zakiis.security.test.model.User;

public class JWTUtilTest {
	
	Logger log = LoggerFactory.getLogger(JWTUtilTest.class);

	@Test
	public void testCreate() {
		User user = newUser();
		Date issueAt = new Date();;
		Date expireAt = DateUtils.addMinutes(issueAt, 30);
		String token = JWTUtil.create()
			.withSubject(String.valueOf(user.getId()))
			.withIssuedAt(issueAt)
			.withExpiresAt(expireAt)
			.withClaim("age", "25")
			.sign(Algorithm.HMAC256(user.getPassword()));
		System.out.println(token);
	}
	
	@Test
	public void testVerify() {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJleHAiOiIyMDIyLTA0LTE1IDA5OjQwOjAyIiwiaWF0IjoiMjAyMi0wNC0xNSAwOToxMDowMiIsImFnZSI6IjI1In0.7nADXMM2AjB5XMOiuXtxsEJholZNj9n_DhKinDrjzDc";
		DecodedJwt decodedJwt = JWTUtil.decode(token);
		User user = getUser(Long.valueOf(decodedJwt.getSubject()));
		try {
			JWTUtil.require(Algorithm.HMAC256(user.getPassword())).verify(decodedJwt);
		} catch (JWTVerificationException e) {
			log.error("jwt token not valid, user id:{}, reason:{}", decodedJwt.getSubject(), e.getMessage());
		}
	}
	
	@Test
	public void testRS256() {
		User user = newUser();
		Date issueAt = new Date();;
		Date expireAt = DateUtils.addMinutes(issueAt, 30);
		KeyPair keyPair = RSAUtil.genKeyPair();
		System.out.println(RSAUtil.formatPubKeyToPEMEncoded(keyPair.getPublic().getEncoded()));
		System.out.println(RSAUtil.formatPrivateKeyToPEMEncoded(keyPair.getPrivate().getEncoded()));
		
		String token = JWTUtil.create()
			.withSubject(String.valueOf(user.getId()))
			.withIssuedAt(issueAt)
			.withExpiresAt(expireAt)
			.withClaim("age", "25")
			.sign(Algorithm.RSA256(RSAAlgorithm.providerForKeys((RSAPublicKey)keyPair.getPublic(), (RSAPrivateKey)keyPair.getPrivate())));
		System.out.println(token);
		
		DecodedJwt decodedJwt = JWTUtil.decode(token);
		try {
			//verify progress needs public key only, you can store it in java back end program or in JWT token 
			JWTUtil.require(Algorithm.RSA256(RSAAlgorithm.providerForKeys((RSAPublicKey)keyPair.getPublic(), null))).verify(decodedJwt);
		} catch (JWTVerificationException e) {
			log.error("jwt token not valid, user id:{}, reason:{}", decodedJwt.getSubject(), e.getMessage());
		}
	}
	
	private User getUser(Long userId) {
		if (userId == 123L) {
			return newUser();
		}
		return null;
	}
	
	private User newUser() {
		User user = new User();
		user.setId(123L);
		user.setPassword("zs23APassx");
		return user;
	}
}
