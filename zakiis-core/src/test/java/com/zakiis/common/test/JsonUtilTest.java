package com.zakiis.common.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zakiis.common.test.model.User;
import com.zakiis.core.util.JsonUtil;

public class JsonUtilTest {
	
	@Test
	public void test() {
		User user = new User();
		user.setName("Zhang San");
		user.setAge(25);
		user.setBirthDate(new Date());
		String content = JsonUtil.toJson(user);
		System.out.println(content);
		
		User user2 = JsonUtil.toObject(content, User.class);
		System.out.println(user2);
		
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		user2.setName("Li Si");
		userList.add(user2);
		String content2 = JsonUtil.toJson(userList);
		System.out.println(content2);
		
		List<User> userList2 = JsonUtil.toObject(content2, new TypeReference<List<User>>() {});
		System.out.println(userList2);
	}

}
