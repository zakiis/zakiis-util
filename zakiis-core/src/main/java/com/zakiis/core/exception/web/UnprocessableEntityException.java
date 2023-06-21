package com.zakiis.core.exception.web;

/**
 * request entity no valid, for example some field is not conformed to validation rules. 
 * HTTP status code should set to 422, @see <a href="https://tools.ietf.org/html/rfc4918#section-11.2">WebDAV</a>
 * @date 2023-06-20 15:18:31
 * @author Liu Zhenghua
 */
public class UnprocessableEntityException extends StandardHttpException {

	private static final long serialVersionUID = -1336117514396422811L;
	public static final int HTTP_STATUS_CODE = 422;
	
	public UnprocessableEntityException() {
		this("Unprocessable Entity");
	}
	
	public UnprocessableEntityException(String message) {
		super(HTTP_STATUS_CODE, message);
	}
}
