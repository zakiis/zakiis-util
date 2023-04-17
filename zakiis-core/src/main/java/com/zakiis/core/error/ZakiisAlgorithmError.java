package com.zakiis.core.error;

public class ZakiisAlgorithmError extends Error implements ZakiisError {

	private static final long serialVersionUID = -1871919744776988160L;

	public ZakiisAlgorithmError() {
		super();
	}

	public ZakiisAlgorithmError(String message, Throwable cause) {
		super(message, cause);
	}

	public ZakiisAlgorithmError(String message) {
		super(message);
	}

	public ZakiisAlgorithmError(Throwable cause) {
		super(cause);
	}
	
}
