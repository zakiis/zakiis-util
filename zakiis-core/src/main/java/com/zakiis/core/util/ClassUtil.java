package com.zakiis.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ClassUtil {
	
	private final static Set<Class<?>> numberClazz = new HashSet<Class<?>>() ;
	
	static {
		numberClazz.add(byte.class);
		numberClazz.add(short.class);
		numberClazz.add(int.class);
		numberClazz.add(long.class);
		numberClazz.add(float.class);
		numberClazz.add(double.class);
	}

	@Deprecated
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
				Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	public static void makeAccessible(Field field, Object obj) {
		if ((!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
				Modifier.isFinal(field.getModifiers())) && !field.canAccess(obj)) {
			field.setAccessible(true);
		}
	}
	
	public static boolean isNumber(Class<?> clazz) {
		boolean isNumber = false;
		if (numberClazz.contains(clazz) || Number.class.isAssignableFrom(clazz)) {
			isNumber = true;
		}
		return isNumber;
	}
	
	public static boolean isBoolean(Class<?> clazz) {
		return boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz);
	}
}
