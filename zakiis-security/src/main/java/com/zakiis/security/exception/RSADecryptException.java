package com.zakiis.security.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class RSADecryptException extends ZakiisRuntimeException {

	private static final long serialVersionUID = 9024074587532446251L;

	public RSADecryptException() {
		super();
	}

	public RSADecryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public RSADecryptException(String message) {
		super(message);
	}

	public RSADecryptException(Throwable cause) {
		super(cause);
	}
	
}
