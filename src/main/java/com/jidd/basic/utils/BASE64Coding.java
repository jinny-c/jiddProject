package com.jidd.basic.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class BASE64Coding {

	public BASE64Coding() {
	}

	/**
	 * Base64加密
	 * 
	 * @param s
	 * @param charset字符码
	 *            如utf-8
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String s, String charset)
			throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(s.getBytes(charset)));
	}

	/**
	 * Base64加密
	 * 
	 * @param s
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String s) {
		return new String(Base64.encodeBase64(s.getBytes()));
	}

	/**
	 * Base64解密
	 * 
	 * @param s
	 * 
	 * @return String
	 */
	public static String decode(String s) {
		try {
			return new String(Base64.decodeBase64(s.getBytes()));
		} catch (Exception ioe) {
		}
		return s;
	}
	/**
	 * Base64解密
	 * 
	 * @param s
	 * @param charset字符码
	 *            如utf-8
	 * @return String
	 */
	public static String decode(String s,String charset) {
		try {
			return new String(Base64.decodeBase64(s.getBytes(charset)));
		} catch (Exception ioe) {
		}
		return s;
	}

}
