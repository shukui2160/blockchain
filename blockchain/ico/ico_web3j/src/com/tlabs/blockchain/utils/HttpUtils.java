package com.tlabs.blockchain.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.www.protocol.http.HttpURLConnection;


/**
 * Created by admin on 2018/7/25.
 */
public class HttpUtils {
    private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 接口调用 GET
     */
    public static String HttpRequestGET(String GET_URL) {
       /*String  GET_URL = "https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apiKey;*/
        /*String  GET_URL = "https://api.etherscan.io/api?module=stats&action=ethprice&apikey="+apiKey;*/
        try {
            URL url = new URL(GET_URL);    // 把字符串转换为URL请求地址
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();// 打开连接
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
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }
        return null;
    }

    public static void interfaceUtil(String path,String data) {
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;
            //请求方式
          conn.setRequestMethod("GET");
//           //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数即数据
            out.print(data);
            //缓冲数据
            out.flush();
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            System.out.println("完整结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {

        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒)

            conn.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒)

            conn.setDoOutput(true); // post请求参数要放在http正文内，顾设置成true，默认是false

            conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true

            conn.setUseCaches(false); // Post 请求不能使用缓存

            // 设定传送的内容类型是可序列化的java对象(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.connect();
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒)

            conn.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒)

            conn.setDoOutput(true); // post请求参数要放在http正文内，顾设置成true，默认是false

            conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true

            conn.setUseCaches(false); // Post 请求不能使用缓存

            // 设定传送的内容类型是可序列化的java对象(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return null;
    }

    public static JSONObject httpRequest2(String requestUrl, String requestMethod, String data) {

        JSONObject jsonObject = null;

        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒)

            conn.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒)

            conn.setDoOutput(true); // post请求参数要放在http正文内，顾设置成true，默认是false

            conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true

            conn.setUseCaches(false); // Post 请求不能使用缓存

            // 设定传送的内容类型是可序列化的java对象(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            conn.connect();

            // 当outputStr不为null时向输出流写数据
            if (null != data) {

                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(data.getBytes("UTF-8"));

                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;

            while ((str = bufferedReader.readLine()) != null) {

                buffer.append(str);
            }
            bufferedReader.close();

            inputStreamReader.close();

            inputStream.close();

            inputStream = null;

            conn.disconnect();

            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return Map<String, String>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {

        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }


    public static JSONObject connectWeiXinInterface(String action, String json) {

        JSONObject response = null;

        try {

            URL url = new URL(action);

            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();

            httpUrlConn.setRequestMethod("POST");

            httpUrlConn.setRequestProperty("Content-Type",

                    "application/x-www-form-urlencoded");

            httpUrlConn.setDoOutput(true);

            httpUrlConn.setDoInput(true);

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒

            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

            httpUrlConn.connect();

            OutputStream os = httpUrlConn.getOutputStream();

            os.write(json.getBytes("UTF-8"));// 传入参数

            InputStream is = httpUrlConn.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            String result = new String(jsonBytes, "UTF-8");

            System.out.println("请求返回结果:" + result);
            response = JSONObject.fromObject(result);

            os.flush();

            os.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return response;

    }



    public static String sendPost(String url, String param) {

        PrintWriter out = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(90000); //设置连接主机超时（单位：毫秒)
            conn.setReadTimeout(90000); //设置从主机读取数据超时（单位：毫秒)
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();

            inputStreamReader.close();

            inputStream.close();

            inputStream = null;
            conn.disconnect();
        }catch (ConnectException ce) {
            log.error("连接服务器 time out.");
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }



    public static String httpURLConectionGET(String GET_URL) {
        try {
            URL url = new URL(GET_URL);    // 把字符串转换为URL请求地址
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();// 打开连接
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
            return  sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }
        return null;
    }

    public static void main(String[] args) {
        String address = "0x3D0ff5939FCaEb054fc69dfB6D751967dC37C096";
        String apikey = "DKX8WHU2W176IS44TNGG76ZK9X4WPG5IGR";
       String url =  "https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apikey;

        httpURLConectionGET(url);

       // interfaceUtil("http://api.map.baidu.com/telematics/v3/weather?location=嘉兴&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ", "");
//        interfaceUtil("http://192.168.10.89:8080/eoffice-restful/resources/sys/oadata", "usercode=10012");
//        interfaceUtil("http://192.168.10.89:8080/eoffice-restful/resources/sys/oaholiday",
//                    "floor=first&year=2017&month=9&isLeader=N");

    }
}
