package com.tlabs.blockchain.utils;

import com.tlabs.blockchain.model.*;
import com.tlabs.blockchain.constant.*;

import java.io.File;
import  java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by admin on 2018/7/25.
 */
public class ETHUtil {

    private static Logger log = Logger.getLogger(ETHUtil.class);
    private static String  apikey ="DKX8WHU2W176IS44TNGG76ZK9X4WPG5IGR";






    /**
     * ���(��ȡһ��eth��ַ�����)
     * @param address
     * @return
     */
    public static String getETHBalance(String address){
        try{
            String balance = HttpUtils.HttpRequestGET("https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apikey);
            System.out.println(balance);
            JSONObject jsonObject = JSONObject.fromObject(balance);
            System.out.println(jsonObject+"---------jsonObject");
            String yue= jsonObject.getString("result");

            return NumberUtil.getDevide(
                    yue, "100000000",8);
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * ����
     * @param address
     * @return
     */
  /*  public static List<Operation> getETHTransactions(String address){
        String url = HttpUtils.httpsRequest("http://api.etherscan.io/api?module=account&action=txlistinternal&address="+address+"&startblock=0&endblock=2702578&sort=asc", "GET", null);
        //String url = HttpUtils.httpsRequest("http://api.etherscan.io/api?module=account&action=txlistinternal&address="+address+"&startblock=0&endblock=2702578&sort=asc");
        JSONObject object = JSONObject.fromObject(url);

        if(url == null){
            boolean nullObject = object.isNullObject();
            System.out.println(nullObject);
        }

        JSONArray ja  = JSONArray.fromObject(object.get("result"));


        List<Operation> list = new ArrayList<Operation>();
        for(int i=0;i<ja.size();i++){
            JSONObject o = JSONObject.fromObject(ja.get(i));
            if(o.containsKey("type")){
                Operation op = new Operation();
                if(o.get("type").toString().startsWith("call")){
                    op.setOperType("ת��");
                }
                if(o.get("type").toString().startsWith("create")){
                    op.setOperType("��ֵ");
                }

                op.setJine(NumberUtil.getDevide((o.get("value").toString()),"1000000000000000000",18));
                //op.setJine(Float.parseFloat(o.get("value").toString())/100000000);
                op.setOperDateTime(new Date(Integer.parseInt(o.get("timeStamp").toString())));
                list.add(op);
            }
        }
        return list;

    }*/

    public static String generateETHAddress () {
        String ETHAddress = null;

        String result = SendRequestUtils.sendGet(RechargeAndWithdrawConstant.URL+RechargeAndWithdrawConstant.NEWADDRESS,"");
        if (StringUtils.isBlank(result)) {
            log.error("����ETH��ַʧ��,��ȡ������Ч!");
            return null;
        }

        JSONObject jsonObject;
        String errorCode = "";
        String errorString = "";

        try {
            jsonObject = JSONObject.fromObject(result);

            errorCode   = jsonObject.getString("errorCode");
            errorString = jsonObject.getString("errorStr");
            ETHAddress  =  jsonObject.getString("data");
        } catch (Exception e) {
            log.error("����ETH��ַʧ��,��ȡ�����쳣!");
            return null;
        }

        if (StringUtils.isBlank(errorCode) || !errorCode.equals("0")) {
            log.error("����ETH��ַʧ��,errorCode:" + errorCode + " errorString:" + errorString);
            return null;
        }

        if (StringUtils.isBlank(ETHAddress)) {
            log.error("����ETH��ַʧ��,��Ч��ַ��errorCode:" + errorCode + " errorString:" + errorString);
            return null;
        }

        return ETHAddress;
    }

    public void  CreateWallet(){
      /*  File file = new File("/home/wallet"); //ָ��һ��Ŀ¼
        WalletUtils.generateNewWalletFile("ָ����Ǯ��������", file, true); //�������ش�����Ǯ���ļ���*/
    }

    public static void main(String[] args) {

        System.out.println(getETHBalance("0x3D0ff5939FCaEb054fc69dfB6D751967dC37C096"));

      /*  System.out.println(getETHTransactions("0x2c1ba59d6f58433fb1eaee7d20b26ed83bda51a3"));*/

    }
}
