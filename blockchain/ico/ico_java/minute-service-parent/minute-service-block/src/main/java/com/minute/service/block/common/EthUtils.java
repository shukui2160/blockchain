package com.minute.service.block.common;

import java.util.Random;

public class EthUtils {

	public static String getRandomPwd(int len) {
		String result = null;
		while (len == 32) {
			result = makeRandomPwd(len);
			if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*")
					&& result.matches(".*[~;<@#:>%^]{1,}.*")) {
				return result;
			}
			result = makeRandomPwd(len);
		}
		return "长度不得少于32位!";
	}

	private static String makeRandomPwd(int len) {
		char charr[] = "abcdefghijk@#$%&ABCDEFGHIJKl@#$%&mnopqrstuvwxyz@#$%&LMNOPQRSTUVWXYZ@#$%&1234567890@#$%&"
				.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int x = 0; x < len; ++x) {
			sb.append(charr[r.nextInt(charr.length)]);
		}
		return sb.toString();
	}
}
