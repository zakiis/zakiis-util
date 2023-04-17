package com.zakiis.core.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SecretFieldTokenizerUtil {
	
	final static Charset charset = StandardCharsets.UTF_8;

	/**
	 * separator tokens by 4 half angle character or 2 full angle character.
	 * @param text origin content
	 * @return tokens
	 */
	public static List<String> tokens(String text) {
		List<String> tokens = new ArrayList<String>();
		if (text == null) {
			return tokens;
		}
		// remove all characters that can't be seen
		text = text.replaceAll("\\s", "");
		if (text.length() == 0) {
			return tokens;
		}
		int tokenSize = 4;
		if (String.valueOf(text.charAt(0)).getBytes(charset).length > 1) {
			tokenSize = 2;
		}
		for (int i = 1; i <= tokenSize; i++) {
			int marker = 0;
			for (int j = i; j <= text.length(); j++) {
				marker++;
				if (marker % tokenSize == 0) {
					tokens.add(text.substring(j - tokenSize, j));
				}
			}
		}
		return tokens;
	}
	
	
	public static List<String> simpleTokens(String text) {
		List<String> tokens = new ArrayList<String>();
		if (text == null) {
			return tokens;
		}
		// remove all characters that can't be seen
		text = text.replaceAll("\\s", "");
		if (text.length() == 0) {
			return tokens;
		}
		int tokenSize = 4;
		if (String.valueOf(text.charAt(0)).getBytes(charset).length > 1) {
			tokenSize = 2;
		}
		for (int i = 1; i <= text.length(); i++) {
			if (i % tokenSize == 0) {
				tokens.add(text.substring(i - tokenSize, i));
			}
		}
		return tokens;
	}
}
