package com.zakiis.core.exception.web;

import com.zakiis.core.exception.ZakiisRuntimeException;

import lombok.Getter;

@Getter
public class StandardHttpException extends ZakiisRuntimeException {

	private static final long serialVersionUID = -5823363350971025449L;
	
	private final int httpStatusCode;
	
	public StandardHttpException(int httpStatusCode, String httpStatusText) {
		super(httpStatusText);
		this.httpStatusCode = httpStatusCode;
	}

}
