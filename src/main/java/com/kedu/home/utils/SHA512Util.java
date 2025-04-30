package com.kedu.home.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Util {

	public static String encrypt(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = plainText.getBytes();
			md.update(bytes);
			byte[] byteData = md.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : byteData) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-512 암호화 실패", e);
		}
	}
}
