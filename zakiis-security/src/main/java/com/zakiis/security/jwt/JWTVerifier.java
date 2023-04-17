package com.zakiis.security.jwt;

import java.util.Date;

import com.zakiis.security.jwt.algorithm.Algorithm;
import com.zakiis.security.jwt.exception.JWTVerificationException;
import com.zakiis.security.jwt.interfaces.DecodedJwt;

public class JWTVerifier {
	
	Algorithm algorithm;
	
	public JWTVerifier(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public DecodedJwt verify(DecodedJwt jwt) {
        verifyAlgorithm(jwt, algorithm);
        verifyDate(jwt);
        algorithm.verify(jwt);
        return jwt;
    }
	
	private void verifyDate(DecodedJwt jwt) {
		Date now = new Date();
		Date start = jwt.getIssuedAt();
		Date end = jwt.getExpiresAt();
		if (start != null && now.compareTo(start) < 0) {
			throw new JWTVerificationException("jwt date is not start.");
		}
		if (end != null && now.compareTo(end) > 0) {
			throw new JWTVerificationException("jwt date has expired.");
		}
	}

	private void verifyAlgorithm(DecodedJwt jwt, Algorithm expectedAlgorithm) {
        if (!expectedAlgorithm.getName().equals(jwt.getAlgorithm())) {
            throw new JWTVerificationException("The provided Algorithm doesn't match the one defined in the JWT's Header.");
        }
    }
}
