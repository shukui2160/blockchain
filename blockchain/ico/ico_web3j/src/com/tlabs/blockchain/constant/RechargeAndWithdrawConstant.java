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
    /**端口**/
    public static final String PORT = properties.getProperty("eth_port");;
    /***URL*/
    public static final String URL = "http://" + HOST + ":" + PORT ;
    /**获取新地址的方法**/
    public static final String NEWADDRESS = "/newaddress";
    /**获取所有地址的方法**/
    public static final String LISTADDRESS = "/listaddress";
    /**获取ETH余额的方法**/
    public static final String GETETHBALANCE = "/getEthBalance";
    /**提现代币的方法**/
    public static final String SENDTOKENS = "/sendTokens";
    /**重新提现的方法**/
    public static final String RESEND = "/resendTx";
    /**代币归集的方法**/
    public static final String COOLECTTOKENS = "/collectTokens";
    /**提现ETH的方法**/
    public static final String SENDETHCOINS = "/sendEthcoins";
    /**获取代币余额的方法**/
    public static final String GETCONINBALANCE = "/getConinBalance";
    /****/
    public static final String ADDCONTRACT = "/addcontract";
}
