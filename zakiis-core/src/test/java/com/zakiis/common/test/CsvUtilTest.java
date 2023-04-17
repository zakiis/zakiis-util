package com.zakiis.common.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.zakiis.common.test.model.User;
import com.zakiis.core.util.CsvUtil;
import com.zakiis.core.util.JsonUtil;

public class CsvUtilTest {

	@Test
	public void testWrite() throws ParseException, FileNotFoundException {
		User user = new User();
		user.setName("zhang\"san, Li");
		user.setBirthDate(new Date());
		user.setAge(23);
		
		User user2 = new User();
		user2.setName("Li si");
		user2.setBirthDate(new Date());
		user2.setAge(25);
		
		User user3 = new User();
		user3.setName("Jack");
		user3.setBirthDate(new Date());
		
		List<User> users = Arrays.asList(user, user2, user3);
		CsvUtil.write(new FileOutputStream(new File("target/user.csv")), users, "utf-8");
	}
	
	@Test
	public void testRead() throws FileNotFoundException {
		List<User> users = CsvUtil.read(new FileInputStream(new File("target/user.csv")), "utf-8", User.class);
		System.out.println(JsonUtil.toJson(users));
	}
}
