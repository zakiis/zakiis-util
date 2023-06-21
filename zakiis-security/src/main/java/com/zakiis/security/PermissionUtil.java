package com.zakiis.security;

import java.util.Set;

import com.zakiis.core.exception.web.ForbiddenException;
import com.zakiis.security.annotation.Permission;

public class PermissionUtil {

	/**
	 * check if current user has privileges on this function
	 * @param ownedFunctionCodes roles that current login user has
	 * @param permission perssion that current method need
	 */
	public static void checkPrivileges(Set<String> ownedFunctionCodes, Permission permission) {
		if (permission == null) {
			throw new ForbiddenException("There is no @Permission on this method.");
		}
		if (permission.bypass()) {
			return;
		}
		if (permission.code() == null || permission.code().length == 0) {
			throw new ForbiddenException("No one can access this method because of the perssion code list is empty, please contact administrator");
		}
		if (ownedFunctionCodes == null || ownedFunctionCodes.size() == 0) {
			throw new ForbiddenException("User don't have privilege on this method, please contact administrator.");
		}
		boolean hasPermission = false;
		for (String code : permission.code()) {
			if (ownedFunctionCodes.contains(code)) {
				hasPermission = true;
				break;
			}
		}
		if (!hasPermission) {
			throw new ForbiddenException("User don't have privilege on this method, please contact administrator.");
		}
	}
}
