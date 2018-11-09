package com.minute.common.eth.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class GasUtils {

	public static BigInteger getGasLimit() {
		// 10 0000
		return new BigInteger("2000000");
	}

	public static BigInteger getGasPrice() {
		// 10 Gwei
		BigInteger gasPrice = Convert.toWei("20", Unit.GWEI).toBigInteger();
		return gasPrice;
	}

	public static void main(String[] args) {
		BigDecimal mount = new BigDecimal((getGasLimit().multiply(getGasPrice())));
		System.out.println(mount.divide(new BigDecimal("1000000000000000000")));
	}
}
