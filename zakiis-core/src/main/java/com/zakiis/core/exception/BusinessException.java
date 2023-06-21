package com.zakiis.core.exception;

import com.zakiis.core.domain.dto.CommonResps;
import com.zakiis.core.domain.dto.Resp;

import lombok.Getter;

@Getter
public class BusinessException extends ZakiisRuntimeException {

	private static final long serialVersionUID = 8078873328268412645L;
	private final String code;
	
	public BusinessException() {
		this(CommonResps.BUSINESS_ERROR);
	}
	
	public BusinessException(Resp resp) {
		this(resp.getCode(), resp.getMessage());
	}
	
	public BusinessException(String message) {
		this(CommonResps.BUSINESS_ERROR.getCode(), message);
	}
	
	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(Throwable e) {
		this(CommonResps.BUSINESS_ERROR, e);
	}
	
	public BusinessException(Resp resp, Throwable e) {
		this(resp.getCode(), resp.getMessage(), e);
	}
	
	public BusinessException(String message, Throwable e) {
		this(CommonResps.BUSINESS_ERROR.getCode(), message, e);
	}
	
	public BusinessException(String code, String message, Throwable e) {
		super(message, e);
		this.code = code;
	}

}
