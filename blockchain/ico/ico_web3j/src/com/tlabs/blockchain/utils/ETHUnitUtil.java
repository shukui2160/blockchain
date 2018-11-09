package com.tlabs.blockchain.utils;

import org.web3j.utils.Convert;

import java.math.BigInteger;

/**
 * Created by admin on 2018/7/27.
 */
public class ETHUnitUtil {

    public static BigInteger ToWai(String eth) {
        BigInteger value = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();
        return value;
    }
}