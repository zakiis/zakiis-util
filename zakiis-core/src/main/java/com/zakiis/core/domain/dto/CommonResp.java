package com.zakiis.core.domain.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommonResp<T> implements Resp, Serializable {

	private static final long serialVersionUID = -8243268190184806944L;
	
	private String code;
	private String message;
	private T data;
	private boolean success;
	
	private CommonResp() {
		
	}
	
	public static CommonResp<Object> success() {
		return of(CommonResps.SUCCESS);
	}
	
	public static <T> CommonResp<T> success(T data) {
		return of(CommonResps.SUCCESS, data);
	}
	
	public static CommonResp<Object> fail(String code, String message) {
		CommonResp<Object> resp = new CommonResp<>();
		resp.code = code;
		resp.message = message;
		return resp;
	}
	
	public static CommonResp<Object> of(Resp resp) {
		return of(resp, null);
	}
	
	public static <T> CommonResp<T> of(Resp resp, T data) {
		CommonResp<T> commonResp = new CommonResp<>();
		commonResp.code = resp.getCode();
		commonResp.message = resp.getMessage();
		commonResp.data = data;
		commonResp.success = resp.isSuccess();
		return commonResp;
	}
}
