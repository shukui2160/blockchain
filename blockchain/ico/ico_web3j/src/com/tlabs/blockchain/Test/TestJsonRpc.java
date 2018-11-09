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
    // 妙兜测试接口
    public static final String POST_URL = "http://121.40.204.191:8180/mdserver/service/installLock";


    public static void httpURLConectionGET(String address ,String apiKey) {
       String  GET_URL = "https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apiKey;
        /*String  GET_URL = "https://api.etherscan.io/api?module=stats&action=ethprice&apikey="+apiKey;*/
        try {
            URL url = new URL(GET_URL);    // 把字符串转换为URL请求地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }
    }

    /**
     * 接口调用  POST
     */
    public static void httpURLConnectionPOST() {
        try {
            URL url = new URL(POST_URL);

            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中

            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);

            // 设置连接输入流为true
            connection.setDoInput(true);

            // 设置请求方式为post
            connection.setRequestMethod("POST");

            // post请求缓存设为false
            connection.setUseCaches(false);

            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);

            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            // ;charset=utf-8 必须要，不然妙兜那边会出现乱码【★★★★★】
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();

            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());

            String app_key = "app_key=" + URLEncoder.encode("4f7bf8c8260124e6e9c6bf094951a111", "utf-8");        // 已修改【改为错误数据，以免信息泄露】
            String agt_num = "&agt_num=" + URLEncoder.encode("10111", "utf-8");                // 已修改【改为错误数据，以免信息泄露】
            String pid = "&pid=" + URLEncoder.encode("BLZXA150401111", "utf-8");                // 已修改【改为错误数据，以免信息泄露】
            String departid = "&departid=" + URLEncoder.encode("10007111", "utf-8");            // 已修改【改为错误数据，以免信息泄露】
            String install_lock_name = "&install_lock_name=" + URLEncoder.encode("南天大门", "utf-8");
            String install_address = "&install_address=" + URLEncoder.encode("北京育新", "utf-8");
            String install_gps = "&install_gps=" + URLEncoder.encode("116.350888,40.011001", "utf-8");
            String install_work = "&install_work=" + URLEncoder.encode("小李", "utf-8");
            String install_telete = "&install_telete=" + URLEncoder.encode("13000000000", "utf-8");
            String intall_comm = "&intall_comm=" + URLEncoder.encode("一切正常", "utf-8");

            // 格式 parm = aaa=111&bbb=222&ccc=333&ddd=444
            String parm = app_key + agt_num + pid + departid + install_lock_name + install_address + install_gps + install_work + install_telete + intall_comm;

            // 将参数输出到连接
            dataout.writeBytes(parm);

            // 输出完成后刷新并关闭流
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)

//            System.out.println(connection.getResponseCode());

            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据

            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
//                sb.append(bf.readLine());
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
            connection.disconnect(); // 销毁连接
            System.out.println(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        /**
         * 向指定URL发送GET方法的请求
         *
         * @param url
         *            发送请求的URL
         * @param param
         *            请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
         * @param charset
         *             发送和接收的格式
         * @return URL 所代表远程资源的响应结果
         */
        public static String sendGet(String url, Map<String,Object> map, String charset){
            StringBuffer sb=new StringBuffer();
            //构建请求参数
            if(map!=null&&map.size()>0){
                Iterator it=map.entrySet().iterator(); //定义迭代器
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
         * 向指定URL发送POST方法的请求
         *
         * @param url
         *            发送请求的URL
         * @param param
         *            请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
         * @param charset
         *             发送和接收的格式
         * @return URL 所代表远程资源的响应结果
         */
        public static String sendPost(String url, Map<String,Object> map,String charset){
            StringBuffer sb=new StringBuffer();
            //构建请求参数
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
         * 向指定URL发送GET方法的请求
         *
         * @param url
         *            发送请求的URL
         * @param param
         *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @param charset
         *             发送和接收的格式
         * @return URL 所代表远程资源的响应结果
         */
        public static String sendGet(String url, String param,String charset) {
            String result = "";
            String line;
            StringBuffer sb=new StringBuffer();
            BufferedReader in = null;
            try {
                String urlNameString = url + "?" + param;
                URL realUrl = new URL(urlNameString);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性 设置请求格式
                conn.setRequestProperty("contentType", charset);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //设置超时时间
                conn.setConnectTimeout(60);
                conn.setReadTimeout(60);
                // 建立实际的连接
                conn.connect();
                // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),charset));
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result=sb.toString();
            } catch (Exception e) {
                System.out.println("发送GET请求出现异常！" + e);
                e.printStackTrace();
            }
            // 使用finally块来关闭输入流
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
         * 向指定 URL 发送POST方法的请求
         *
         * @param url
         *            发送请求的 URL
         * @param param
         *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @param charset
         *             发送和接收的格式
         * @return 所代表远程资源的响应结果
         */
        public static String sendPost(String url, String param,String charset) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
            String line;
            StringBuffer sb=new StringBuffer();
            try {
                URL realUrl = new URL(url);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性 设置请求格式
                conn.setRequestProperty("contentType", charset);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //设置超时时间
                conn.setConnectTimeout(60);
                conn.setReadTimeout(60);
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应    设置接收格式
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),charset));
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result=sb.toString();
            } catch (Exception e) {
                System.out.println("发送 POST请求出现异常!"+e);
                e.printStackTrace();
            }
            //使用finally块来关闭输出流、输入流
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