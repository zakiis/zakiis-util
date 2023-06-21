package com.zakiis.core.exception.web;

/**
 * user has login, but no privileges on this resource.
 * should set HTTP status code 403 on this error.
 * @date 2023-06-20 15:07:32
 * @author Liu Zhenghua
 */
public class ForbiddenException extends StandardHttpException {

	private static final long serialVersionUID = -1336117514396422811L;
	public static final int HTTP_STATUS_CODE = 403;
	
	public ForbiddenException() {
		this("Unauthorized");
	}
	
	public ForbiddenException(String message) {
		super(HTTP_STATUS_CODE, message);
	}
	
}
