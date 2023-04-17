package com.zakiis.core.util;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.zakiis.core.exception.ZakiisRuntimeException;

public class AssertUtil {

	public static void notEmpty(Collection<?> c, String msg) {
		if (c == null || c.size() == 0) {
			throw new ZakiisRuntimeException(msg);
		}
	}
	
	public static void notEmpty(String obj, String msg) {
		if (StringUtils.isBlank(obj)) {
			throw new ZakiisRuntimeException(msg);
		}
	}
	
	public static void notNull(Object o, String msg) {
		if (o == null) {
			throw new ZakiisRuntimeException(msg);
		}
	}
}
