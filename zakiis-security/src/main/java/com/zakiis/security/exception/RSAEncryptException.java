package com.zakiis.security.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class RSAEncryptException extends ZakiisRuntimeException {

	private static final long serialVersionUID = 7308981109861518883L;

	public RSAEncryptException() {
		super();
	}

	public RSAEncryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public RSAEncryptException(String message) {
		super(message);
	}

	public RSAEncryptException(Throwable cause) {
		super(cause);
	}
	
}
