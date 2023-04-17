package com.zakiis.security.codec;

public class HexUtil {

	public static String toHexString(byte[] buf) {
		StringBuffer buffer = new StringBuffer(buf.length * 2);
		for (byte b : buf) {
			String hexString = Integer.toHexString(b & 0xFF);
			if (hexString.length() == 1) {
				buffer.append('0');
			}
			buffer.append(hexString);
		}
		return buffer.toString();
	}

	public static byte[] toByteArray(String hexString) {
		if (hexString.length() % 2 != 0) {
			throw new IllegalArgumentException("The length of hex string must be an odd number.");
		}
		byte[] result = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			result[(i / 2)] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
		}
		return result;
	}
}
