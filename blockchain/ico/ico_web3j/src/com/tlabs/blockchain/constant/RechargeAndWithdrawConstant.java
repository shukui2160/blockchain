package com.tlabs.blockchain.constant;

import com.tlabs.blockchain.utils.*;
import java.util.Properties;

/**
 * Created by admin on 2018/7/25.
 */
public class RechargeAndWithdrawConstant {
    private static final Properties properties = PropertiesUtil.getProperties("config.properties");
    /**IP**/
    public static final String HOST = properties.getProperty("eth_ip");
    //public static final String HOST = "10.10.1.50";
    /**�˿�**/
    public static final String PORT = properties.getProperty("eth_port");;
    /***URL*/
    public static final String URL = "http://" + HOST + ":" + PORT ;
    /**��ȡ�µ�ַ�ķ���**/
    public static final String NEWADDRESS = "/newaddress";
    /**��ȡ���е�ַ�ķ���**/
    public static final String LISTADDRESS = "/listaddress";
    /**��ȡETH���ķ���**/
    public static final String GETETHBALANCE = "/getEthBalance";
    /**���ִ��ҵķ���**/
    public static final String SENDTOKENS = "/sendTokens";
    /**�������ֵķ���**/
    public static final String RESEND = "/resendTx";
    /**���ҹ鼯�ķ���**/
    public static final String COOLECTTOKENS = "/collectTokens";
    /**����ETH�ķ���**/
    public static final String SENDETHCOINS = "/sendEthcoins";
    /**��ȡ�������ķ���**/
    public static final String GETCONINBALANCE = "/getConinBalance";
    /****/
    public static final String ADDCONTRACT = "/addcontract";
}
