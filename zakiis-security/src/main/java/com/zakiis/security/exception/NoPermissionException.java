package com.zakiis.security.exception;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class NoPermissionException extends ZakiisRuntimeException {

	private static final long serialVersionUID = -8502692815664404722L;

	public NoPermissionException() {
		super();
	}

	public NoPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPermissionException(String message) {
		super(message);
	}

	public NoPermissionException(Throwable cause) {
		super(cause);
	}

}
