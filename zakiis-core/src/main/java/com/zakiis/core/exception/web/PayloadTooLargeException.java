package com.zakiis.core.exception.web;

/**
 * The payload of the request is too large, for example: upload file over the exceeded.
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.11">
 * @date 2023-06-20 15:37:38
 * @author Liu Zhenghua
 */
public class PayloadTooLargeException extends StandardHttpException {

	private static final long serialVersionUID = 8369678192535468397L;
	public static final int HTTP_STATUS_CODE = 413;
	
	public PayloadTooLargeException() {
		this("Payload Too Large");
	}
	
	public PayloadTooLargeException(String message) {
		super(HTTP_STATUS_CODE, message);
	}
	
}
