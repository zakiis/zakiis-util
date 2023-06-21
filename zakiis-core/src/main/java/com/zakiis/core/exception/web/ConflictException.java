package com.zakiis.core.exception.web;

/**
 * The ConflictException indicates that the request could not be completed due to a conflict with the current state of the target resource
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.8">HTTP/1.1: Semantics and Content, section 6.5.8</a>
 * @date 2023-06-20 15:44:18
 * @author Liu Zhenghua
 */
public class ConflictException extends StandardHttpException {

	private static final long serialVersionUID = 7756714137107646806L;
	public static final int HTTP_STATUS_CODE = 409;
	
	public ConflictException() {
		this("Conflict");
	}
	
	public ConflictException(String message) {
		super(HTTP_STATUS_CODE, message);
	}
	
}
