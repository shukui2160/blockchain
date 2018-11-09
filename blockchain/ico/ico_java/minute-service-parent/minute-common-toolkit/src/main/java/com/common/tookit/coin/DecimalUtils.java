package com.common.tookit.coin;

import java.math.BigDecimal;

public class DecimalUtils {

	// 如果dec1 >= dec2 则返回true 否则返回false
	public static boolean compare(String dec1, String dec2) {
		int result = new BigDecimal(dec1).compareTo(new BigDecimal(dec2));
		if (result >= 0) {
			return true;
		} else {
			return false;
		}
	}

	// 如果dec1 > dec2 则返回true 否则返回false
	public static boolean compareTT(String dec1, String dec2) {
		int result = new BigDecimal(dec1).compareTo(new BigDecimal(dec2));
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	// dec1 和 dec2 做乘法运算
	public static String multiply(String dec1, String dec2) {
		String value =  new BigDecimal(dec1).multiply(new BigDecimal(dec2)).toPlainString();
		return removeZero(value);
	}

	// dec1 和 dec2 做加法运算
	public static String add(String dec1, String dec2) {
		String value= new BigDecimal(dec1).add(new BigDecimal(dec2)).toPlainString();
		return removeZero(value);
	}

	// dec1 和 dec2 做减法运算
	public static String subtract(String dec1, String dec2) {
		String value = new BigDecimal(dec1).subtract(new BigDecimal(dec2)).toPlainString();
		return removeZero(value);
	}

	// dec1 和 dec2 做除法运算
	public static String divide(String dec1, String dec2) {
		String value= new BigDecimal(dec1).divide(new BigDecimal(dec2), 18, BigDecimal.ROUND_HALF_UP).toPlainString();
		return removeZero(value);
	}

	public static void main(String[] args) {
		System.out.println(toWei("0.001"));

	}

	// dec转换为 wei
	public static String toWei(String dec) {
		String value =  new BigDecimal(dec).multiply(new BigDecimal(Math.pow(10, 18))).stripTrailingZeros().toPlainString();
		return removeZero(value);
	}

	// wei转换为eth
	public static String toEth(String dec) {
		String value =  new BigDecimal(dec).divide(new BigDecimal(Math.pow(10, 18))).toPlainString();
		return removeZero(value);
	}

	// token发行总量 dec1 除 10 的 decimals平方
	public static String toToken(String dec1, String decimals) {
		return new BigDecimal(dec1).divide(new BigDecimal(Math.pow(10, Double.valueOf(decimals)))).toPlainString();
	}

	// token
	public static String toDecimals(String dec) {
		return new BigDecimal(Math.pow(10, Double.valueOf(dec))).toPlainString();
	}

	// 移除字符串后面的0
	public static String removeZero(String value) {
		return new BigDecimal(value).stripTrailingZeros().toPlainString();
	}

}
