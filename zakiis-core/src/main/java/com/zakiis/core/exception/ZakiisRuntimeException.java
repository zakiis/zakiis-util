package com.zakiis.core.exception;

public class ZakiisRuntimeException extends RuntimeException implements ZakiisException {

	private static final long serialVersionUID = 4020978105220290106L;

	public ZakiisRuntimeException() {
		super("unknown error");
	}
	
	public ZakiisRuntimeException(String message) {
		super(message);
	}
	
	public ZakiisRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public ZakiisRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
