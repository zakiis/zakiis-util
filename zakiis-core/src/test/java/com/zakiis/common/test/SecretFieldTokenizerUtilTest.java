package com.zakiis.common.test;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.zakiis.core.util.SecretFieldTokenizerUtil;

public class SecretFieldTokenizerUtilTest {

	@Test
	public void test() {
		List<String> phoeTokens = SecretFieldTokenizerUtil.tokens("186 1234 5678");
		System.out.println(phoeTokens);
		List<String> addressTokens = SecretFieldTokenizerUtil.tokens("车公庙 泰然七路1号 博今国际");
		System.out.println(addressTokens);
		List<String> idTokens = SecretFieldTokenizerUtil.tokens("230182197510240691");
		System.out.println(idTokens);
		List<String> noTokens = SecretFieldTokenizerUtil.tokens("128");
		System.out.println(noTokens);
		List<String> oneTokens = SecretFieldTokenizerUtil.tokens("1281");
		System.out.println(oneTokens);
		
		List<String> phoneSimpleTokens = SecretFieldTokenizerUtil.simpleTokens("1861234");
		System.out.println(phoneSimpleTokens);
		List<String> addressSimpleTokens = SecretFieldTokenizerUtil.simpleTokens("泰然七路1号");
		System.out.println(addressSimpleTokens);
		List<String> idSimpleTokens = SecretFieldTokenizerUtil.simpleTokens("19751024");
		System.out.println(idSimpleTokens);
	}
}
