package com.zakiis.security.test;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.zakiis.core.util.JsonUtil;
import com.zakiis.security.AESUtil;
import com.zakiis.security.CipherUtil;
import com.zakiis.security.test.model.Address;
import com.zakiis.security.test.model.Order;
import com.zakiis.security.test.model.User;

public class CipherUtilTest {
	
	static byte[] secretKey;
	static byte[] iv;
	
	
	@BeforeAll
	public static void setup() {
		secretKey = AESUtil.genKey();
		iv = "0123456789123456".getBytes();
		CipherUtil.init(secretKey, iv, false);
	}

	@Test
	public void test() {
		Address address1 = new Address();
		address1.setCountry("中国");
		address1.setProvince("广东");
		address1.setCity("深圳");
		address1.setRegion("罗湖");
		address1.setStreet("黄贝");
		CipherUtil.encrypt(address1, false);
		System.out.println(JsonUtil.toJson(address1));
		
		CipherUtil.decrypt(address1);
		System.out.println(JsonUtil.toJson(address1));
		
		User user = new User();
		user.setUsername("zhang san");
		user.setAge("25");
		user.setResidence(address1);
		CipherUtil.encrypt(user, false);
		System.out.println(JsonUtil.toJson(user));
		
		User user2 = new User();
		user2.setUsername("li si");
		user2.setAge("25");
		CipherUtil.encrypt(user2, false);
		System.out.println(JsonUtil.toJson(user2));
		
		CipherUtil.decrypt(user);
		System.out.println(JsonUtil.toJson(user));
		
		Order order = new Order();
		order.setOrderNo("100000001");
		order.setMoney(BigDecimal.TEN);
		CipherUtil.encrypt(order, false);
		//need check class Order in exluceClazz hash set
		CipherUtil.decrypt(order);
	}
}
