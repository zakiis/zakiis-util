package com.zakiis.security.jwt;

import org.apache.commons.lang3.StringUtils;

import com.zakiis.core.exception.ZakiisRuntimeException;
import com.zakiis.security.jwt.algorithm.Algorithm;
import com.zakiis.security.jwt.interfaces.DecodedJwt;

/**
 * JSON Web Token
 * https://datatracker.ietf.org/doc/html/rfc7519
 * @author 10901
 */
public class JWTUtil {

	public static JWTCreator.Builder create() {
		return new JWTCreator.Builder();
	}
	
	public static DecodedJwt decode(String token) {
		if (StringUtils.isBlank(token)) {
			throw new ZakiisRuntimeException("JWT token can't be empty");
		}
		return new JWTDecoder(token);
	}

	public static JWTVerifier require(Algorithm algorithm) {
		return new JWTVerifier(algorithm);
	}
}
