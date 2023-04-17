package com.zakiis.common.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.zakiis.core.util.SimilarityUtil;

public class SimilarityUtilTest {

	@Test
	public void test() {
		int similarity = SimilarityUtil.compare("Hello", "Hello");
		assertEquals(similarity, 100);
		similarity = SimilarityUtil.compare("Hello", "Hallo");
		assertEquals(similarity, 80);
		similarity = SimilarityUtil.compare("Hello", "eo");
		assertEquals(similarity, 40);
		similarity = SimilarityUtil.compare("Hello", "");
		assertEquals(similarity, 0);
		similarity = SimilarityUtil.compare("Hello", null);
		assertEquals(similarity, 0);
		similarity = SimilarityUtil.compare(null, null);
		assertEquals(similarity, 0);
	}
}
