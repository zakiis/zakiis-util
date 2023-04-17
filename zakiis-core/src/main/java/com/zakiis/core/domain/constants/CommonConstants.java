package com.zakiis.core.domain.constants;

public interface CommonConstants {

	/** rfc6648规范已经不建议自定义请求头以X-开头了，原因是如果未来标准化了后，会同时存在带X-和不带的两个请求头，详细见：https://datatracker.ietf.org/doc/html/rfc6648 */
	final static String TRACE_ID_HEADER_NAME = "Trace-Id";
	
	final static String TRACE_ID_PARAM_NAME = "traceId";
	
	final static String JWT_TOKEN = "X-JWT-TOKEN";
	
}
