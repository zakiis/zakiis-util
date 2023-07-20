package com.zakiis.common.test;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.zakiis.core.map.AutoFlushedHashMap;
import com.zakiis.core.map.ReactorAutoFlushedHashMap;
import com.zakiis.core.map.SizedHashMap;

import reactor.core.publisher.Mono;

public class MapTest {

	@Test
	public void testSizedHashMap() {
		Map<String, Object> map = new SizedHashMap<String, Object>(2);
		map.put("1001", "zhangsan");
		map.put("1002", "lisi");
		System.out.println(map);
		map.put("1003", "wangwu");
		System.out.println(map);
		
	}
	
	@Test
	public void testAutoFlushedHashMap() throws InterruptedException {
		Map<String, String> map = new AutoFlushedHashMap<String, String>(2, 5000L, MapTest::flushValue);
		map.put("1001", "zhangsan");
		Thread.sleep(3000L);
		map.put("1002", "lisi");
		System.out.println(map);
		Thread.sleep(3000L);
		System.out.println("key 1001 would be expired: " + map);
		System.out.println("the value of 1003 is: " + map.get("1003"));
		System.out.println(map);
		System.out.println("the value of 1001 is: " + map.get("1001"));
		System.out.println("key 1002 would be evicted cause of the threshold: " + map);
		Thread.sleep(5100L);
		System.out.println("all keys expired: " + map);
	}
	
	@Test
	public void testReactorAutoFlushedHashMap() throws InterruptedException {
		Map<String, Mono<String>> map = new ReactorAutoFlushedHashMap<String, String>(2, 5000L, MapTest::monoFlushValue);
		new Thread(() -> {
			System.out.println("1The value of 1001 is " + map.get("1001").block());
		}).start();
		new Thread(() -> {
			System.out.println("2The value of 1001 is " + map.get("1001").block());
		}).start();
		new Thread(() -> {
			System.out.println("3The value of 1001 is " + map.get("1001").block());
		}).start();
//		System.out.println("The value of 1001 is " + map.get("1001").block());
//		System.out.println("The value of 1001 is " + map.get("1001").block());
//		System.out.println("The value of 1001 is " + map.get("1001").block());
		Thread.sleep(10100L);
//		System.out.println("The value of 1001 is " + map.get("1001").block());
	}
	
	public static String flushValue(String key) {
		if (key.equals("1001")) {
			return "flushed zhangsan";
		} else if (key.equals("1002")) {
			return "flushed lisi";
		} else if (key.equals("1003")) {
			return "flushed wangwu";
		}
		return "not found";
	}
	
	public static Mono<String> monoFlushValue(String key) {
		System.out.println("flush value for key: " + key);
		if (key.equals("1001")) {
			return Mono.defer(() -> {
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("flushed...");
//				return Mono.just("flushed zhangsan");
				return Mono.error(new RuntimeException("test error"));
			});
		} else if (key.equals("1002")) {
			return Mono.just("flushed lisi");
		} else if (key.equals("1003")) {
			return Mono.just("flushed wangwu");
		}
		return Mono.just("not found");
	}
}
