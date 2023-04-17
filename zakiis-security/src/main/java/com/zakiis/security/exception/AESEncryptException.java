package com.zakiis.security.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class AESEncryptException extends ZakiisRuntimeException {

	private static final long serialVersionUID = -1216765497646444798L;

	public AESEncryptException() {
		super();
	}

	public AESEncryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public AESEncryptException(String message) {
		super(message);
	}

	public AESEncryptException(Throwable cause) {
		super(cause);
	}
	
}
