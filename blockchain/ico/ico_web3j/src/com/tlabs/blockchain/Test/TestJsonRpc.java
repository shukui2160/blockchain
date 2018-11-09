package com.tlabs.blockchain.Test;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhuzhisheng
 * @Description
 * @date on 2016/12/31.
 */
public class TestJsonRpc {


    //    public static final String POST_URL = "http://112.4.27.9/mall-back/if_user/store_list";
    // ����Խӿ�
    public static final String POST_URL = "http://121.40.204.191:8180/mdserver/service/installLock";


    public static void httpURLConectionGET(String address ,String apiKey) {
       String  GET_URL = "https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apiKey;
        /*String  GET_URL = "https://api.etherscan.io/api?module=stats&action=ethprice&apikey="+apiKey;*/
        try {
            URL url = new URL(GET_URL);    // ���ַ���ת��ΪURL�����ַ
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// ������
            connection.connect();// ���ӻỰ
            // ��ȡ������
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// ѭ����ȡ��
                sb.append(line);
            }
            br.close();// �ر���
            connection.disconnect();// �Ͽ�����
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ʧ��!");
        }
    }

    /**
     * �ӿڵ���  POST
     */
    public static void httpURLConnectionPOST() {
        try {
            URL url = new URL(POST_URL);

            // ��url �� open�������ص�urlConnection  ����ǿתΪHttpURLConnection����  (��ʶһ��url�����õ�Զ�̶�������)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// ��ʱcnnectionֻ��Ϊһ�����Ӷ���,��������

            // �������������Ϊtrue,Ĭ��false (post �����������ķ�ʽ��ʽ�Ĵ��ݲ���)
            connection.setDoOutput(true);

            // ��������������Ϊtrue
            connection.setDoInput(true);

            // ��������ʽΪpost
            connection.setRequestMethod("POST");

            // post���󻺴���Ϊfalse
            connection.setUseCaches(false);

            // ���ø�HttpURLConnectionʵ���Ƿ��Զ�ִ���ض���
            connection.setInstanceFollowRedirects(true);

            // ��������ͷ����ĸ������� (����Ϊ�������ݵ�����,����Ϊ����urlEncoded�������from����)
            // application/x-javascript text/xml->xml���� application/x-javascript->json���� application/x-www-form-urlencoded->������
            // ;charset=utf-8 ����Ҫ����Ȼ��Ǳ߻�������롾�����
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // �������� (����δ��ʼ,ֱ��connection.getInputStream()��������ʱ�ŷ���,���ϸ��������������ڴ˷���֮ǰ����)
            connection.connect();

            // �������������,�����������������Я���Ĳ���,(�������Ϊ?���������)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());

            String app_key = "app_key=" + URLEncoder.encode("4f7bf8c8260124e6e9c6bf094951a111", "utf-8");        // ���޸ġ���Ϊ�������ݣ�������Ϣй¶��
            String agt_num = "&agt_num=" + URLEncoder.encode("10111", "utf-8");                // ���޸ġ���Ϊ�������ݣ�������Ϣй¶��
            String pid = "&pid=" + URLEncoder.encode("BLZXA150401111", "utf-8");                // ���޸ġ���Ϊ�������ݣ�������Ϣй¶��
            String departid = "&departid=" + URLEncoder.encode("10007111", "utf-8");            // ���޸ġ���Ϊ�������ݣ�������Ϣй¶��
            String install_lock_name = "&install_lock_name=" + URLEncoder.encode("�������", "utf-8");
            String install_address = "&install_address=" + URLEncoder.encode("��������", "utf-8");
            String install_gps = "&install_gps=" + URLEncoder.encode("116.350888,40.011001", "utf-8");
            String install_work = "&install_work=" + URLEncoder.encode("С��", "utf-8");
            String install_telete = "&install_telete=" + URLEncoder.encode("13000000000", "utf-8");
            String intall_comm = "&intall_comm=" + URLEncoder.encode("һ������", "utf-8");

            // ��ʽ parm = aaa=111&bbb=222&ccc=333&ddd=444
            String parm = app_key + agt_num + pid + departid + install_lock_name + install_address + install_gps + install_work + install_telete + intall_comm;

            // ���������������
            dataout.writeBytes(parm);

            // �����ɺ�ˢ�²��ر���
            dataout.flush();
            dataout.close(); // ��Ҫ���׺��Բ��� (�ر���,�м�!)

//            System.out.println(connection.getResponseCode());

            // ���ӷ�������,�����������Ӧ  (�����ӻ�ȡ������������װΪbufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // �����洢��Ӧ����

            // ѭ����ȡ��,��������β��
            while ((line = bf.readLine()) != null) {
//                sb.append(bf.readLine());
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();    // ��Ҫ���׺��Բ��� (�ر���,�м�!)
            connection.disconnect(); // ��������
            System.out.println(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        /**
         * ��ָ��URL����GET����������
         *
         * @param url
         *            ���������URL
         * @param param
         *            ����Map�������������Ӧ���� {"name1":"value1","name2":"value2"}����ʽ��
         * @param charset
         *             ���ͺͽ��յĸ�ʽ
         * @return URL ������Զ����Դ����Ӧ���
         */
        public static String sendGet(String url, Map<String,Object> map, String charset){
            StringBuffer sb=new StringBuffer();
            //�����������
            if(map!=null&&map.size()>0){
                Iterator it=map.entrySet().iterator(); //���������
                while(it.hasNext()){
                    Map.Entry  er= (Map.Entry) it.next();
                    sb.append(er.getKey());
                    sb.append("=");
                    sb.append(er.getValue());
                    sb.append("&");
                }
            }
            return  sendGet(url,sb.toString(), charset);
        }


        /**
         * ��ָ��URL����POST����������
         *
         * @param url
         *            ���������URL
         * @param param
         *            ����Map�������������Ӧ���� {"name1":"value1","name2":"value2"}����ʽ��
         * @param charset
         *             ���ͺͽ��յĸ�ʽ
         * @return URL ������Զ����Դ����Ӧ���
         */
        public static String sendPost(String url, Map<String,Object> map,String charset){
            StringBuffer sb=new StringBuffer();
            //�����������
            if(map!=null&&map.size()>0){
                for (Map.Entry<String, Object> e : map.entrySet()) {
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(e.getValue());
                    sb.append("&");
                }
            }
            return  sendPost(url,sb.toString(),charset);
        }


        /**
         * ��ָ��URL����GET����������
         *
         * @param url
         *            ���������URL
         * @param param
         *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
         * @param charset
         *             ���ͺͽ��յĸ�ʽ
         * @return URL ������Զ����Դ����Ӧ���
         */
        public static String sendGet(String url, String param,String charset) {
            String result = "";
            String line;
            StringBuffer sb=new StringBuffer();
            BufferedReader in = null;
            try {
                String urlNameString = url + "?" + param;
                URL realUrl = new URL(urlNameString);
                // �򿪺�URL֮�������
                URLConnection conn = realUrl.openConnection();
                // ����ͨ�õ��������� ���������ʽ
                conn.setRequestProperty("contentType", charset);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //���ó�ʱʱ��
                conn.setConnectTimeout(60);
                conn.setReadTimeout(60);
                // ����ʵ�ʵ�����
                conn.connect();
                // ���� BufferedReader����������ȡURL����Ӧ,���ý��ո�ʽ
                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),charset));
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result=sb.toString();
            } catch (Exception e) {
                System.out.println("����GET��������쳣��" + e);
                e.printStackTrace();
            }
            // ʹ��finally�����ر�������
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return result;
        }

        /**
         * ��ָ�� URL ����POST����������
         *
         * @param url
         *            ��������� URL
         * @param param
         *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
         * @param charset
         *             ���ͺͽ��յĸ�ʽ
         * @return ������Զ����Դ����Ӧ���
         */
        public static String sendPost(String url, String param,String charset) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
            String line;
            StringBuffer sb=new StringBuffer();
            try {
                URL realUrl = new URL(url);
                // �򿪺�URL֮�������
                URLConnection conn = realUrl.openConnection();
                // ����ͨ�õ��������� ���������ʽ
                conn.setRequestProperty("contentType", charset);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //���ó�ʱʱ��
                conn.setConnectTimeout(60);
                conn.setReadTimeout(60);
                // ����POST�������������������
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // ��ȡURLConnection�����Ӧ�������
                out = new PrintWriter(conn.getOutputStream());
                // �����������
                out.print(param);
                // flush������Ļ���
                out.flush();
                // ����BufferedReader����������ȡURL����Ӧ    ���ý��ո�ʽ
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),charset));
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result=sb.toString();
            } catch (Exception e) {
                System.out.println("���� POST��������쳣!"+e);
                e.printStackTrace();
            }
            //ʹ��finally�����ر��������������
            finally{
                try{
                    if(out!=null){
                        out.close();
                    }
                    if(in!=null){
                        in.close();
                    }
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            return result;
        }

        public static void main(String[] args) {
         httpURLConectionGET("0x3D0ff5939FCaEb054fc69dfB6D751967dC37C096","DKX8WHU2W176IS44TNGG76ZK9X4WPG5IGR");
       /* httpURLConnectionPOST();*/



    }
}