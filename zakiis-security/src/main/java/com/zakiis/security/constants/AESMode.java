package com.zakiis.security.constants;

public interface AESMode {

	static final String ECB_PKCS7PADDING = "AES/CBC/PKCS7Padding";
	static final String ECB_NOPADDING = "AES/CBC/NoPadding";
	
	static final String CBC_PKCS7PADDING = "AES/CBC/PKCS7Padding";
	static final String CBC_NOPADDING = "AES/CBC/NoPadding";
	
	/** cipher feed back, defaults cfb128 */
	static final String CFB_PKCS7PADDING = "AES/CFB/PKCS7Padding";
	static final String CFB_NOPADDING = "AES/CFB/NoPadding";
	
	static final String CFB1_PKCS7PADDING = "AES/CFB1/PKCS7Padding";
	static final String CFB1_NOPADDING = "AES/CFB1/NoPadding";
	static final String CFB8_PKCS7PADDING = "AES/CFB8/PKCS7Padding";
	static final String CFB8_NOPADDING = "AES/CFB8/NoPadding";
}
