package com.tlabs.blockchain.utils;

import java.io.IOException;
import java.math.*;
import java.util.List;

import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.geth.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;

import org.web3j.protocol.parity.Parity;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.Credentials;


/**
 * Created by admin on 2018/7/25.
 */
public class Web3jUtil {

    private static String ip = "http://192.168.2.5:8545";
    private static String key = "";
    private static String keystore = "";
    private static Web3j web3jClient = Web3jUtil.initWeb3j();
    private static Admin adminClient = Web3jUtil.initAdmin();
    private static Admin gethClient = Web3jUtil.initGeth();
    private static Parity parityClient = Web3jUtil.initParity();

    private Web3jUtil(){}
    private volatile static Web3j web3j;


    //region 初始化

    public static Web3j getClient(){
        if(web3j==null){
            synchronized (Web3jUtil.class){
                if(web3j==null){
                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }
        return web3j;
    }

    /**
     * 初始化web3j普通api调用
     *
     * @return web3j
     */
    public static Web3j initWeb3j() {
        return Web3j.build(new HttpService(ip));
    }

    /**
     * 初始化personal级别的操作对象
     * @return Geth
     */
    public static Geth initGeth(){
        return Geth.build(new HttpService(ip));
    }

    /**
     * 初始化admin级别操作的对象
     * @return Admin
     */
    public static Admin initAdmin(){
        return Admin.build(new HttpService(ip));
    }

    /**
     * 初始化parity级别操作的对象
     * @return Admin
     */
    public static Parity initParity(){
        return Parity.build(new HttpService(ip));
    }

    //endregion

    //region 凭证

    /**根据钱包的私钥创建凭证*/
    public Credentials CreateCredentials() {
        Credentials credentials = Credentials.create(key);
        return credentials;
    }

    /**根据钱包的key store创建凭证*/
    public Credentials CreateCredentialsKeystore() {
        Credentials credentials = Credentials.create(key);
        return credentials;
    }


    /**根据钱包的路径和密码创建凭证*/
    /* 钱包路径/path/to/walletfile*/
    public Credentials CreateCredentialsPassword(String walletPath,String password){
        Credentials credentials = null;
        try{
            credentials = WalletUtils.loadCredentials(password, walletPath);
        }
        catch(Exception e){

        }
        return credentials;
    }


    //endregion

    //region gas

    public int GetGasPrice(){
        return 123;
    }

    public int GetGasLimit(){
        return 123;
    }

    //endregion

    //region 账户

    //获取钱包账户列表
    public List<String> getAccountlist(){

        try{
            return  adminClient.personalListAccounts().send().getAccountIds();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入密码创建地址
     *
     * @param password 密码（建议同一个平台的地址使用一个相同的，且复杂度较高的密码）
     * @return 地址hash
     * @throws IOException
     */
    public static String newAccount(String password) throws IOException {
        Admin admin = initAdmin();
        Request<?, NewAccountIdentifier> request = admin.personalNewAccount(password);
        NewAccountIdentifier result = request.send();
        return result.getAccountId();

    }

    /**创建账户*/
    public  static String createAccount(String password){
        try {
            NewAccountIdentifier newAccountIdentifier = adminClient.personalNewAccount(password).send();
            if(newAccountIdentifier!=null){
                String accountId = newAccountIdentifier.getAccountId();
                return  accountId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解锁账户，发送交易前需要对账户进行解锁
     *
     * @param address  地址
     * @param password 密码
     * @param duration 解锁有效时间，单位秒
     * @return
     * @throws IOException
     */
    public static Boolean unlockAccount(String address, String password, BigInteger duration) throws IOException {
        Request<?, PersonalUnlockAccount> request = adminClient.personalUnlockAccount(address, password, duration);
        PersonalUnlockAccount account = request.send();
        return account.accountUnlocked();
    }

    /**
     * 账户锁定，使用完成之后需要锁定
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static Boolean lockAccount(String address) throws IOException {
        Geth geth = initGeth();
        Request<?, BooleanResponse> request = geth.personalLockAccount(address);
        BooleanResponse response = request.send();
        return response.success();
    }



    //endregion

    //region 转账

    /**转账*/
   /* * 这里返回的是交易的id，另一个系统需要不断的访问这个地址，查询是否到账。*/
    public String Transfer(String from,String toAddress,String key,String eth){
        //设置需要的矿工费
        BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
        String transactionHash = "";

        //转账人账户地址
        /*String ownAddress = "0xD1c82c71cC567d63Fd53D5B91dcAC6156E5B96B3";*/
     /*   String ownAddress = from;*/
        //被转人账户地址
       /* String toAddress = "0x6e27727bbb9f0140024a62822f013385f4194999";*/
      /*  String toAddress = to;*/
        //转账人私钥

        try{
            //获取凭证 - 私钥
            Credentials credentials = this.CreateCredentials();

            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            //eth转化成wei
            BigInteger value =  ETHUnitUtil.ToWai(eth);
            /*转账三步骤：*/
            /*创建原始交易，签名，发送交易*/
            /*创建原始交易*/
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);
            //签名Transaction，这里要对交易做签名
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            //发送交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
             transactionHash = ethSendTransaction.getTransactionHash();


        }
        catch(Exception e){

        }
        //获得到transactionHash后就可以到以太坊的网站上查询这笔交易的状态了
        return transactionHash;
    }


    //from:转出方账户
    //password:转出方密码
    //addrTo:收款账户
    //value:转账额
    public  static String transferEth(String from,String password,String to,BigInteger value) throws  Exception {
         String transactionhash = "";
        EthGetTransactionCount ethGetTransactionCount = web3jClient.ethGetTransactionCount(
                from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount().add(new BigInteger("1"));
        System.out.println(nonce);
        PersonalUnlockAccount personalUnlockAccount = adminClient.personalUnlockAccount(from,password).send();
        System.out.println(personalUnlockAccount.accountUnlocked());
        if (personalUnlockAccount.accountUnlocked())
        {
            BigInteger gasPrice = Contract.GAS_PRICE;
            BigInteger gasLimit = Contract.GAS_LIMIT.divide(new BigInteger("2"));
            synchronized(Web3jUtil.class) {
                Transaction transaction = Transaction.createEtherTransaction(from,nonce,gasPrice,gasLimit,to,value);
                EthSendTransaction transactionResponse = web3jClient.ethSendTransaction(transaction).sendAsync().get();
                if(transactionResponse.hasError()){
                    String message=transactionResponse.getError().getMessage();
                    System.out.println("transaction failed,info:"+message);
                    //如果报错返回error或者其他定义好的错误信息；
                    transactionhash = "error";
                    return message;
                }else{
                    String hash = transactionResponse.getTransactionHash();
                    System.out.println("transaction from "+from+" to "+to+" amount:"+value);
                    transactionhash = hash;
                  /*  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
                    //writeFile("transaction from "+from+" to "+to+" amount:"+value+" time:"+df.format(new Date()));
                }
            }

        }
        return transactionhash;
    }

    /*根据交易hash获取交易进度等信息，主系统会轮训调用查询交易状态；*/
    public String  EthGetTransactionInfo(){

        return "";
    }

      /* 正常情况下一个整数最多只能放在long类型之中，但是如果现在有如下的一个数字：
        1111111111111111111111111111111111111111111111111
        根本就是无法保存的，所以为了解决这样的问题，在java中引入了两个大数的操作类：
        操作整型：BigInteger
        操作小数：BigDecimal
        当然了，这些大数都会以字符串的形式传入。*/
     /*   BigInteger bi1 = new BigInteger("123456789") ;	// 声明BigInteger对象
        BigInteger bi2 = new BigInteger("987654321") ;	// 声明BigInteger对象
        System.out.println("加法操作：" + bi2.add(bi1)) ;	// 加法操作
        System.out.println("减法操作：" + bi2.subtract(bi1)) ;	// 减法操作
        System.out.println("乘法操作：" + bi2.multiply(bi1)) ;	// 乘法操作
        System.out.println("除法操作：" + bi2.divide(bi1)) ;	// 除法操作
        System.out.println("最大数：" + bi2.max(bi1)) ;	 // 求出最大数
        System.out.println("最小数：" + bi2.min(bi1)) ;	 // 求出最小数
        BigInteger result[] = bi2.divideAndRemainder(bi1) ;	// 求出余数的除法操作
        System.out.println("商是：" + result[0] +
                "；余数是：" + result[1]) ;*/
    public void TransferDemo(String from,String to,BigInteger gasprice,BigInteger gaslimit,BigInteger nonce,String value) {

        try {

            BigInteger myValue =  ETHUnitUtil.ToWai(value);
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasprice, gaslimit, to, myValue);

            Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
            TransactionReceipt transactionReceipt = Transfer.sendFunds(web3jClient, credentials, "0x<address>|<ensName>", BigDecimal.valueOf(1.0), Convert.Unit.ETHER).send();

        } catch (Exception e) {

        }
    }




    //endregion

    //region 区块


    /**
     * 获得当前区块高度
     *
     * @return 当前区块高度
     * @throws IOException
     */
    public static BigInteger getCurrentBlockNumber() throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
        return request.send().getBlockNumber();
    }

    /**
     * 获得ethblock
     *
     * @param blockNumber 根据区块编号
     * @return
     * @throws IOException
     */
    public static EthBlock getBlockEthBlock(Integer blockNumber) throws IOException {
        Web3j web3j = initWeb3j();

        DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(blockNumber);
        Request<?, EthBlock> request = web3j.ethGetBlockByNumber(defaultBlockParameter, true);
        EthBlock ethBlock = request.send();

        return ethBlock;
    }

    //endregion

    //region 交易

    /**
     * 指定地址发送交易所需nonce获取
     *
     * @param address 待发送交易地址
     * @return
     * @throws IOException
     */
    public static BigInteger getNonce(String address) throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthGetTransactionCount> request = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST);
        return request.send().getTransactionCount();
    }

    /**
     * 根据hash值获取交易
     *
     * @param hash
     * @return
     * @throws IOException
     */
    public static EthTransaction getTransactionByHash(String hash) throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(hash);
        return request.send();
    }

    /**
     * 发送交易并获得交易hash值
     *
     * @param transaction
     * @param password
     * @return
     * @throws IOException
     */
    public static String sendTransaction(Transaction transaction, String password) throws IOException {
        Admin admin = initAdmin();
        Request<?, EthSendTransaction> request = admin.personalSendTransaction(transaction, password);
        EthSendTransaction ethSendTransaction = request.send();
        return ethSendTransaction.getTransactionHash();

    }

    //endregion

    //region 基本信息

    /**获取版本号*/
    public static String GetVersion(){
        String str = "";
        Web3j web3j = Web3jUtil.getClient();
        Web3ClientVersion web3ClientVersion;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            str = clientVersion;
        } catch (IOException e) {
            e.printStackTrace();
            str = "异常";
        }
        return str;
    }


    //endregion

    //region RPC
    //endregion

    //region token

    /*校验地址是否合法*/
    public Boolean CheckAddress(){
        return true;
    }

    /*根据token获取其相关信息*/
   /* public Token GetTokenInfo(){
        Token token  = new Token();
        return token;
    }
*/
    //获取token余额
    public void GetTokenBalance(){

    }



    //endregion

    //region 智能合约
    //endregion

    //region 钱包

    public void ListenWalletTractions(){

    }

    //endregion

    //region main函数

    public static void main(String[] args) {
        //获取版本测试
       /* System.out.println(Web3jUtil.GetVersion());*/

        //生成账户地址测试
        String password = "123456";
        String account = "";
        account = Web3jUtil.createAccount(password);
       /* System.out.println(account);*/
        BigInteger bi1 = new BigInteger("10000000000");
        String tid = "";
        try{
            for(int i = 0 ;i<2;i++){
                tid =  Web3jUtil.transferEth("0x05286c22018ec0092f38819b0ff479ce7576ef88","123","0xf8684b70f21cd2760bb9d3f00422900e68dc1fa3",bi1);
                System.out.println(tid);
            }

        }
        catch (Exception e){
        }
    }

    //endregion

    //region demo

    //endregion

}
