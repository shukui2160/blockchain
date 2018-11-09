package com.minute.common.eth.test;

import java.math.BigInteger;

import com.minute.common.eth.EthTemplate;

public class Test4 {
	public static void main(String[] args) {

		EthTemplate template = new EthTemplate("http://106.2.3.17:8546");
		// String txHash =
		// template.ethTransfer("0x726269ccb6f4cc791950ab4039464b93a431fbde", "123",
		// "0xf6963e954231cc15806207d767a593757b57055f", new
		// BigInteger(DecimalUtils.toWei("1")), "0.002");
		
		String txHash = template.tokenTransfer("0xf6963e954231cc15806207d767a593757b57055f", "123",
				"0x726269ccb6f4cc791950ab4039464b93a431fbde", "0xf7a77af1d87494ebea19e13701f47cacc9249bb1", new BigInteger("10000"), "0.002");
		System.out.println(txHash);
	}

}
