package com.zakiis.security.jwt.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class JWTVerificationException extends ZakiisRuntimeException {

	private static final long serialVersionUID = 1417569065969159115L;

	public JWTVerificationException() {
		super();
	}

	public JWTVerificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JWTVerificationException(String message) {
		super(message);
	}

	public JWTVerificationException(Throwable cause) {
		super(cause);
	}

	
}
