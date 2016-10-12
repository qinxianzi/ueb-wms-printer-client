package com.ueb.wms.printer.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UebMd5Util {
	private static Logger logger = LoggerFactory.getLogger(UebMd5Util.class);
	private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static MessageDigest getInstance() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("MD5");
	}

	public static String getMd5(String password, String salt) {
		try {
			MessageDigest md5 = getInstance();
			md5.update(password.getBytes());
			md5.update(salt.getBytes());
			return byteArrayToHexString(md5.digest());
		} catch (Exception e) {
			logger.info("用户登陆--md5加密密码出现异常，异常信息是：{}", e.getMessage());
		}
		return "";
	}
}
