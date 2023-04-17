package com.zakiis.common.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.zakiis.core.util.CaptchaUtil;

public class CaptchaUtilTest {

	@Test
	public void test() throws IOException {
		FileOutputStream fos = new FileOutputStream(new File("target/captcha.png"));
		CaptchaUtil.genImgCode(120, 40, 0, fos);
	}
}
