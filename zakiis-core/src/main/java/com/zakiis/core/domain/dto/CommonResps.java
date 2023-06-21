package com.zakiis.core.domain.dto;

public enum CommonResps implements Resp {
	
	SUCCESS("000000", "success"),
	UNAUTHORIZED("401", "unauthrozied"),
	PARAMETER_INVALID("102000", "parameter invalid"),
	RUNTIME_ERROR("103000", "runtime error"),
	BUSINESS_ERROR("200000", "business error"),
	UNKNOWN_ERROR("999999", "unknown error")
	;

	private String code;
	private String message;
	private CommonResps(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public boolean isSuccess() {
		return SUCCESS.getCode().equals(code);
	}

}
