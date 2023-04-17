package com.zakiis.security.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.security.PermissionUtil;
import com.zakiis.security.annotation.Permission;
import com.zakiis.security.exception.NoPermissionException;

public class PermissionUtilTest {
	
	Logger log = LoggerFactory.getLogger(PermissionUtilTest.class);

	@Test
	public void test() throws NoSuchMethodException, SecurityException {
		Set<String> userRoles = new HashSet<String>(Arrays.asList("USER_QUERY", "ORDER_QUERY"));
		
		Permission permission = PermissionUtilTest.class.getMethod("orderQuery").getAnnotation(Permission.class);
		PermissionUtil.checkPrivileges(userRoles, permission);
		log.info("User have access on order query method");
		
		try {
			Permission permission2 = PermissionUtilTest.class.getMethod("orderModify").getAnnotation(Permission.class);
			PermissionUtil.checkPrivileges(userRoles, permission2);
		} catch (NoPermissionException e) {
			log.info("user don't have access on order modify method");
		}
	}
	
	@Permission(code = {"ORDER_QUERY", "ORDER_MANAGE"})
	public void orderQuery() {
		
	}
	
	@Permission(code = {"ORDER_MANAGE"})
	public void orderModify() {
		
	}
}
