package com.zakiis.common.test;

import org.junit.jupiter.api.Test;

import com.zakiis.core.util.SnowFlakeUtil;

public class SnowFlakeUtilTest {

	@Test
	public void test() throws InterruptedException {
		SnowFlakeUtil.init(2, 1);
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 0; j < 30; j++) {
						long id = SnowFlakeUtil.generate();
						System.out.println(id);
					}
				}
			}, "thread-" + i);
			thread.start();
			thread.join();
		}
	}
}
