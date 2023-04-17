package com.zakiis.security;

import java.util.Set;

import com.zakiis.security.annotation.Permission;
import com.zakiis.security.exception.NoPermissionException;

public class PermissionUtil {

	/**
	 * check if current user has privileges on this function
	 * @param ownedFunctionCodes roles that current login user has
	 * @param permission perssion that current method need
	 */
	public static void checkPrivileges(Set<String> ownedFunctionCodes, Permission permission) {
		if (permission == null) {
			throw new NoPermissionException("There is no @Permission on this method.");
		}
		if (permission.bypass()) {
			return;
		}
		if (permission.code() == null || permission.code().length == 0) {
			throw new NoPermissionException("No roles can access this method, please contact administrator");
		}
		if (ownedFunctionCodes == null || ownedFunctionCodes.size() == 0) {
			throw new NoPermissionException("User don't have privilege on this method, please contact administrator.");
		}
		boolean hasPermission = false;
		for (String code : permission.code()) {
			if (ownedFunctionCodes.contains(code)) {
				hasPermission = true;
				break;
			}
		}
		if (!hasPermission) {
			throw new NoPermissionException("User don't have privilege on this method, please contact administrator.");
		}
	}
}
