package com.tlabs.blockchain.utils;

import com.tlabs.blockchain.model.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by admin on 2018/7/25.
 */
public class BTCUtils {

    public static String getBTCBalance(String address) {
        return NumberUtil.getDevide(HttpUtils.httpsRequest("https://blockchain.info/q/addressbalance/"+address+"?confirmations=2", "GET", null),"100000000",8);
    }


    public static List<Operation> getBTCTransactions(String address) {
        JSONObject object = JSONObject.fromObject(HttpUtils.httpsRequest("https://blockchain.info/rawaddr/"+address,"GET",null));
        JSONArray ja  = JSONArray.fromObject(object.get("txs"));
        List list = new ArrayList();
        for(int i=0;i<ja.size();i++){
            JSONObject o = JSONObject.fromObject(ja.get(i));
            if(o.containsKey("result")){
                Operation op = new Operation();
                if(o.get("result").toString().startsWith("-")){
                    op.setOperType("×ªÕË");
                }else{
                    op.setOperType("³äÖµ");
                }

                op.setJine(Float.parseFloat(o.get("result").toString())/100000000);
                op.setOperDateTime(new Date(Integer.parseInt(o.get("time").toString())));
                list.add(op);
            }
        }
        return list;
    }

    public static void main(String[] args){
        System.out.println(getBTCTransactions("1HwQDjFQQevAWtVsWnbUpPjT3doYHsUq1S"));
    }
}
