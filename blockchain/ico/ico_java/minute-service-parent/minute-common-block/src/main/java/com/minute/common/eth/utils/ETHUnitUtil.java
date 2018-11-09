package com.minute.common.eth.utils;

import java.math.BigInteger;
import org.web3j.utils.Convert;

public class ETHUnitUtil {
	public static BigInteger ToWai(String eth) {
		BigInteger value = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();
		return value;
	}
}
