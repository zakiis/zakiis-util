package com.zakiis.core.exception.web;

/**
 * When the resource need login and current no login info, this exception should be thrown.
 * should set HTTP status 403 on exception handler
 * @date 2023-06-20 14:58:28
 * @author Liu Zhenghua
 */
public class UnauthorizedException extends StandardHttpException {

	private static final long serialVersionUID = 3917675604244728352L;
	public static final int HTTP_STATUS_CODE = 401;
	
	public UnauthorizedException() {
		this("Unauthorized");
	}
	
	public UnauthorizedException(String message) {
		super(HTTP_STATUS_CODE, message);
	}
}
