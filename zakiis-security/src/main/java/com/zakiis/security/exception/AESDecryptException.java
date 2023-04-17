package com.zakiis.security.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class AESDecryptException extends ZakiisRuntimeException {

	private static final long serialVersionUID = -1936610023921911891L;

	public AESDecryptException() {
		super();
	}

	public AESDecryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public AESDecryptException(String message) {
		super(message);
	}

	public AESDecryptException(Throwable cause) {
		super(cause);
	}
	
}
