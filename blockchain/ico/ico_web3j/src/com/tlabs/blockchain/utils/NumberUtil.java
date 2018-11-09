package com.tlabs.blockchain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by admin on 2018/7/25.
 */
public class NumberUtil {

    public static String getDevide(String par1,String par2,int scale){
        MathContext mc = new MathContext(scale, RoundingMode.HALF_DOWN);
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.divide(bd2,mc).toString();
    }
    public static String getDevide(String par1,String par2){
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.divide(bd2,0).toString();
    }

    public static String getDevide(String par1,float par2){
        BigDecimal bd1 = new BigDecimal(par1);
        return bd1.divide(new BigDecimal(par2),0).toString();
    }
    public static String getAdd(String par1,String par2){
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.add(bd2).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getSubtruct(String par1,String par2){
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.subtract(bd2).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getMultiply(String par1,String par2){
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.multiply(bd2).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String getMultiply(String par1,float par2){
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.multiply(bd2).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String getMod(String par1,String par2){
        BigDecimal bd1 = new BigDecimal(par1);
        BigDecimal bd2 = new BigDecimal(par2);
        return bd1.divideAndRemainder(bd2)[1].toString();

    }

    // 大于返回1，等于返回0，小于返回-1
    public static int compare(String c1,String c2){
        BigDecimal bignum1 = new BigDecimal(c1);
        BigDecimal bignum2 = new BigDecimal(c2);
        return bignum1.compareTo(bignum2);
    }
}
