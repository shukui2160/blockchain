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


    //region ��ʼ��

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
     * ��ʼ��web3j��ͨapi����
     *
     * @return web3j
     */
    public static Web3j initWeb3j() {
        return Web3j.build(new HttpService(ip));
    }

    /**
     * ��ʼ��personal����Ĳ�������
     * @return Geth
     */
    public static Geth initGeth(){
        return Geth.build(new HttpService(ip));
    }

    /**
     * ��ʼ��admin��������Ķ���
     * @return Admin
     */
    public static Admin initAdmin(){
        return Admin.build(new HttpService(ip));
    }

    /**
     * ��ʼ��parity��������Ķ���
     * @return Admin
     */
    public static Parity initParity(){
        return Parity.build(new HttpService(ip));
    }

    //endregion

    //region ƾ֤

    /**����Ǯ����˽Կ����ƾ֤*/
    public Credentials CreateCredentials() {
        Credentials credentials = Credentials.create(key);
        return credentials;
    }

    /**����Ǯ����key store����ƾ֤*/
    public Credentials CreateCredentialsKeystore() {
        Credentials credentials = Credentials.create(key);
        return credentials;
    }


    /**����Ǯ����·�������봴��ƾ֤*/
    /* Ǯ��·��/path/to/walletfile*/
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

    //region �˻�

    //��ȡǮ���˻��б�
    public List<String> getAccountlist(){

        try{
            return  adminClient.personalListAccounts().send().getAccountIds();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * �������봴����ַ
     *
     * @param password ���루����ͬһ��ƽ̨�ĵ�ַʹ��һ����ͬ�ģ��Ҹ��ӶȽϸߵ����룩
     * @return ��ַhash
     * @throws IOException
     */
    public static String newAccount(String password) throws IOException {
        Admin admin = initAdmin();
        Request<?, NewAccountIdentifier> request = admin.personalNewAccount(password);
        NewAccountIdentifier result = request.send();
        return result.getAccountId();

    }

    /**�����˻�*/
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
     * �����˻������ͽ���ǰ��Ҫ���˻����н���
     *
     * @param address  ��ַ
     * @param password ����
     * @param duration ������Чʱ�䣬��λ��
     * @return
     * @throws IOException
     */
    public static Boolean unlockAccount(String address, String password, BigInteger duration) throws IOException {
        Request<?, PersonalUnlockAccount> request = adminClient.personalUnlockAccount(address, password, duration);
        PersonalUnlockAccount account = request.send();
        return account.accountUnlocked();
    }

    /**
     * �˻�������ʹ�����֮����Ҫ����
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

    //region ת��

    /**ת��*/
   /* * ���ﷵ�ص��ǽ��׵�id����һ��ϵͳ��Ҫ���ϵķ��������ַ����ѯ�Ƿ��ˡ�*/
    public String Transfer(String from,String toAddress,String key,String eth){
        //������Ҫ�Ŀ󹤷�
        BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
        String transactionHash = "";

        //ת�����˻���ַ
        /*String ownAddress = "0xD1c82c71cC567d63Fd53D5B91dcAC6156E5B96B3";*/
     /*   String ownAddress = from;*/
        //��ת���˻���ַ
       /* String toAddress = "0x6e27727bbb9f0140024a62822f013385f4194999";*/
      /*  String toAddress = to;*/
        //ת����˽Կ

        try{
            //��ȡƾ֤ - ˽Կ
            Credentials credentials = this.CreateCredentials();

            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            //ethת����wei
            BigInteger value =  ETHUnitUtil.ToWai(eth);
            /*ת�������裺*/
            /*����ԭʼ���ף�ǩ�������ͽ���*/
            /*����ԭʼ����*/
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                    nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);
            //ǩ��Transaction������Ҫ�Խ�����ǩ��
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            //���ͽ���
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
             transactionHash = ethSendTransaction.getTransactionHash();


        }
        catch(Exception e){

        }
        //��õ�transactionHash��Ϳ��Ե���̫������վ�ϲ�ѯ��ʽ��׵�״̬��
        return transactionHash;
    }


    //from:ת�����˻�
    //password:ת��������
    //addrTo:�տ��˻�
    //value:ת�˶�
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
                    //���������error������������õĴ�����Ϣ��
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

    /*���ݽ���hash��ȡ���׽��ȵ���Ϣ����ϵͳ����ѵ���ò�ѯ����״̬��*/
    public String  EthGetTransactionInfo(){

        return "";
    }

      /* ���������һ���������ֻ�ܷ���long����֮�У�����������������µ�һ�����֣�
        1111111111111111111111111111111111111111111111111
        ���������޷�����ģ�����Ϊ�˽�����������⣬��java�����������������Ĳ����ࣺ
        �������ͣ�BigInteger
        ����С����BigDecimal
        ��Ȼ�ˣ���Щ�����������ַ�������ʽ���롣*/
     /*   BigInteger bi1 = new BigInteger("123456789") ;	// ����BigInteger����
        BigInteger bi2 = new BigInteger("987654321") ;	// ����BigInteger����
        System.out.println("�ӷ�������" + bi2.add(bi1)) ;	// �ӷ�����
        System.out.println("����������" + bi2.subtract(bi1)) ;	// ��������
        System.out.println("�˷�������" + bi2.multiply(bi1)) ;	// �˷�����
        System.out.println("����������" + bi2.divide(bi1)) ;	// ��������
        System.out.println("�������" + bi2.max(bi1)) ;	 // ��������
        System.out.println("��С����" + bi2.min(bi1)) ;	 // �����С��
        BigInteger result[] = bi2.divideAndRemainder(bi1) ;	// ��������ĳ�������
        System.out.println("���ǣ�" + result[0] +
                "�������ǣ�" + result[1]) ;*/
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

    //region ����


    /**
     * ��õ�ǰ����߶�
     *
     * @return ��ǰ����߶�
     * @throws IOException
     */
    public static BigInteger getCurrentBlockNumber() throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
        return request.send().getBlockNumber();
    }

    /**
     * ���ethblock
     *
     * @param blockNumber ����������
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

    //region ����

    /**
     * ָ����ַ���ͽ�������nonce��ȡ
     *
     * @param address �����ͽ��׵�ַ
     * @return
     * @throws IOException
     */
    public static BigInteger getNonce(String address) throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthGetTransactionCount> request = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST);
        return request.send().getTransactionCount();
    }

    /**
     * ����hashֵ��ȡ����
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
     * ���ͽ��ײ���ý���hashֵ
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

    //region ������Ϣ

    /**��ȡ�汾��*/
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
            str = "�쳣";
        }
        return str;
    }


    //endregion

    //region RPC
    //endregion

    //region token

    /*У���ַ�Ƿ�Ϸ�*/
    public Boolean CheckAddress(){
        return true;
    }

    /*����token��ȡ�������Ϣ*/
   /* public Token GetTokenInfo(){
        Token token  = new Token();
        return token;
    }
*/
    //��ȡtoken���
    public void GetTokenBalance(){

    }



    //endregion

    //region ���ܺ�Լ
    //endregion

    //region Ǯ��

    public void ListenWalletTractions(){

    }

    //endregion

    //region main����

    public static void main(String[] args) {
        //��ȡ�汾����
       /* System.out.println(Web3jUtil.GetVersion());*/

        //�����˻���ַ����
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
