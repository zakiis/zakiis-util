package com.zakiis.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zakiis.core.util.JsonUtil;
import com.zakiis.security.codec.Base64Util;
import com.zakiis.security.jwt.algorithm.Algorithm;
import com.zakiis.security.jwt.interfaces.PublicClaims;

public class JWTCreator {

	private final Algorithm algorithm;
	private final String headerJson;
	private final String payloadJson;

	private JWTCreator(Algorithm algorithm, Map<String, Object> headerClaims, Map<String, Object> payloadClaims) {
		this.algorithm = algorithm;
		headerJson = JsonUtil.toJson(headerClaims);
		payloadJson = JsonUtil.toJson(payloadClaims);
	}

	public static class Builder {

		private final Map<String, Object> headerClaims;
		private final Map<String, Object> payloadClaims;

		public Builder() {
			headerClaims = new HashMap<String, Object>();
			payloadClaims = new HashMap<String, Object>();
		}

		private void addClaim(String name, Object value) {
			if (value == null) {
				payloadClaims.remove(name);
				return;
			}
			payloadClaims.put(name, value);
		}

		public Builder withClaim(String name, String value) {
			addClaim(name, value);
			return this;
		}

		public Builder withAudience(String... audience) {
			addClaim(PublicClaims.AUDIENCE, audience);
			return this;
		}

		public Builder withExpiresAt(Date expiresAt) {
			addClaim(PublicClaims.EXPIRES_AT, expiresAt);
			return this;
		}

		public Builder withNotBefore(Date notBefore) {
			addClaim(PublicClaims.NOT_BEFORE, notBefore);
			return this;
		}

		public Builder withIssuedAt(Date issuedAt) {
			addClaim(PublicClaims.ISSUED_AT, issuedAt);
			return this;
		}

		public Builder withKeyId(String keyId) {
			this.headerClaims.put(PublicClaims.KEY_ID, keyId);
			return this;
		}

		public Builder withIssuer(String issuer) {
			addClaim(PublicClaims.ISSUER, issuer);
			return this;
		}

		public Builder withJwtId(String jwtId) {
			addClaim(PublicClaims.JWT_ID, jwtId);
			return this;
		}

		public Builder withSubject(String subject) {
			addClaim(PublicClaims.SUBJECT, subject);
			return this;
		}

		public Builder withHeader(Map<String, Object> headerClaims) {
			if (headerClaims == null) {
				return this;
			}
			for (Map.Entry<String, Object> entry : headerClaims.entrySet()) {
				if (entry.getValue() == null) {
					this.headerClaims.remove(entry.getKey());
				} else {
					this.headerClaims.put(entry.getKey(), entry.getValue());
				}
			}
			return this;
		}

		public String sign(Algorithm algorithm) {
			if (algorithm == null) {
				throw new IllegalArgumentException("The Algorithm cannot be null.");
			}
			headerClaims.put(PublicClaims.ALGORITHM, algorithm.getName());
			if (!headerClaims.containsKey(PublicClaims.TYPE)) {
				headerClaims.put(PublicClaims.TYPE, "JWT");
			}
			String signingKeyId = algorithm.getSigningKeyId();
			if (signingKeyId != null) {
				withKeyId(signingKeyId);
			}
			return new JWTCreator(algorithm, headerClaims, payloadClaims).sign();
		}
	}

	private String sign() {
		String header = Base64Util.encodeToBase64URL(headerJson.getBytes(StandardCharsets.UTF_8));
		String payload = Base64Util.encodeToBase64URL(payloadJson.getBytes(StandardCharsets.UTF_8));
		byte[] signatureBytes = algorithm.sign(header.getBytes(StandardCharsets.UTF_8),
				payload.getBytes(StandardCharsets.UTF_8));
		String signature = Base64Util.encodeToBase64URL(signatureBytes);
		return String.format("%s.%s.%s", header, payload, signature);
	}
}
