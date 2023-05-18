package com.zakiis.security.aes;

import java.util.Arrays;

public class ZeroSaltGenerator {

	 public static byte[] generateSalt(final int lengthBytes) {
        final byte[] result = new byte[lengthBytes];
        Arrays.fill(result, (byte)0);
        return result;
    }
}
