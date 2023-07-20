package com.zakiis.core.loader;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class EnhancedServiceNotFoundException extends ZakiisRuntimeException {

	private static final long serialVersionUID = 1394173298701283790L;

	public EnhancedServiceNotFoundException() {
		super();
	}

	public EnhancedServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EnhancedServiceNotFoundException(String message) {
		super(message);
	}

	public EnhancedServiceNotFoundException(Throwable cause) {
		super(cause);
	}

	
}
