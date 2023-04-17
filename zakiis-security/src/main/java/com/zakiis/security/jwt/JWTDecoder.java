package com.zakiis.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import com.zakiis.security.codec.Base64Util;
import com.zakiis.security.jwt.interfaces.DecodedJwt;
import com.zakiis.security.jwt.interfaces.Header;
import com.zakiis.security.jwt.interfaces.Payload;

public class JWTDecoder implements DecodedJwt {

	private final String[] parts;
	private final Header header;
	private final Payload payload;
	
	JWTDecoder(String token) {
		this.parts = token.split("\\.");
		String headerJson = new String(Base64Util.decodeFromBase64URL(parts[0]), StandardCharsets.UTF_8);
		String payloadJson = new String(Base64Util.decodeFromBase64URL(parts[1]), StandardCharsets.UTF_8);
		header = JWTParser.parseHeader(headerJson);
		payload = JWTParser.parsePayload(payloadJson);
	}

	@Override
	public String getIssuer() {
		return payload.getIssuer();
	}

	@Override
	public String getSubject() {
		return payload.getSubject();
	}

	@Override
	public List<String> getAudience() {
		return payload.getAudience();
	}

	@Override
	public Date getExpiresAt() {
		return payload.getExpiresAt();
	}

	@Override
	public Date getNotBefore() {
		return payload.getNotBefore();
	}

	@Override
	public Date getIssuedAt() {
		return payload.getIssuedAt();
	}

	@Override
	public String getId() {
		return payload.getId();
	}

	@Override
	public String getClaim(String name) {
		return payload.getClaim(name);
	}

	@Override
	public String getAlgorithm() {
		return header.getAlgorithm();
	}

	@Override
	public String getType() {
		return header.getType();
	}

	@Override
	public String getContentType() {
		return header.getContentType();
	}

	@Override
	public String getKeyId() {
		return header.getKeyId();
	}

	@Override
	public String getHeaderClaim(String name) {
		return header.getHeaderClaim(name);
	}

	@Override
	public String getToken() {
		return String.format("%s.%s.%s", parts[0], parts[1], parts[2]);
	}

	@Override
	public String getHeader() {
		return parts[0];
	}

	@Override
	public String getPayload() {
		return parts[1];
	}

	@Override
	public String getSignature() {
		return parts[2];
	}
}
