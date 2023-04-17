package com.zakiis.security.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class DigestException extends ZakiisRuntimeException {

	private static final long serialVersionUID = -7784892570774092272L;

	public DigestException() {
		super();
	}

	public DigestException(String message, Throwable cause) {
		super(message, cause);
	}

	public DigestException(String message) {
		super(message);
	}

	public DigestException(Throwable cause) {
		super(cause);
	}
	
}
