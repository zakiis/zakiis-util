package com.zakiis.common.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.zakiis.core.ThreadContext;


public class ThreadContextTest {

	@Test
	public void test() {
		ThreadContext context = new ThreadContext();
		context.put("key1", "hello");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				context.put("key1", "world");
				String value = context.get("key1", String.class);
				assertEquals(value, "world");
			}
		}).start();
		String value = context.get("key1", String.class);
		assertEquals(value, "hello");
		context.clear();
		assertNull(context.get("key1", String.class));
	}

}
