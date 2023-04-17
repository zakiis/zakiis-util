package com.zakiis.core.util;

/**
 * 计算两个字符串的相似度
 * 方法：先计算字串相差几个字符能一样，再往上扩充
 * @author 10901
 */
public class SimilarityUtil {

	/**
	 * 相似度已换算成百分比的值
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compare(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return 0;
		}
		int len1 = str1.length();
		int len2 = str2.length();
		//数组大小比字符串长度大1， 第0个表示空串
		int[][] diffArr = new int[len1 + 1][len2 + 1];
		//初始化diff数组，当str1为空串时，str2的每个字串与str1的最小变换字符个数为str2字串的长度
		for (int i = 0; i <= len1; i++) {
			diffArr[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			diffArr[0][j] = j;
		}
		//计算字串的最小差异值
		int diff = 0;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					diff = 0;
				} else {
					diff = 1;
				}
				diffArr[i][j] = min(diffArr[i - 1][j] + 1, diffArr[i][j - 1] + 1, diffArr[i-1][j-1] + diff);
			}
		}
		//计算str1和str2相似度
		diff = diffArr[len1][len2];
		return (int)((1 - (double)diff / Math.max(len1, len2)) * 100);
	}
	
	private static int min(int... intArr) {
		int min = Integer.MAX_VALUE;
		for (int value : intArr) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}
}
