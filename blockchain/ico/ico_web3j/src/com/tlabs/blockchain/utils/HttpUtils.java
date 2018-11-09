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
     * �ӿڵ��� GET
     */
    public static String HttpRequestGET(String GET_URL) {
       /*String  GET_URL = "https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apiKey;*/
        /*String  GET_URL = "https://api.etherscan.io/api?module=stats&action=ethprice&apikey="+apiKey;*/
        try {
            URL url = new URL(GET_URL);    // ���ַ���ת��ΪURL�����ַ
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();// ������
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
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ʧ��!");
        }
        return null;
    }

    public static void interfaceUtil(String path,String data) {
        try {
            URL url = new URL(path);
            //�򿪺�url֮�������
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;
            //����ʽ
          conn.setRequestMethod("GET");
//           //����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //�����Ƿ���httpUrlConnection����������Ƿ��httpUrlConnection���룬���ⷢ��post�����������������
            //��õ�Http�����޷���get��post��get������Ի�ȡ��̬ҳ�棬Ҳ���԰Ѳ�������URL�ִ����棬���ݸ�servlet��
            //post��get�� ��֮ͬ������post�Ĳ������Ƿ���URL�ִ����棬���Ƿ���http����������ڡ�
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            //�����������������
            out.print(data);
            //��������
            out.flush();
            //��ȡURLConnection�����Ӧ��������
            InputStream is = conn.getInputStream();
            //����һ���ַ�������
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
            //�ر���
            is.close();
            //�Ͽ����ӣ����д�ϣ�disconnect���ڵײ�tcp socket���ӿ���ʱ���жϡ�������ڱ������߳�ʹ�þͲ��жϡ�
            //�̶����̵߳Ļ��������disconnect�����ӻ����ֱ࣬���շ�������Ϣ��д��disconnect������һЩ��
            conn.disconnect();
            System.out.println("��������");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {

        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // ������SSLContext�����еõ�SSLSocketFactory����
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(30000); // ��������������ʱ����λ������)

            conn.setReadTimeout(30000); // ���ô�������ȡ���ݳ�ʱ����λ������)

            conn.setDoOutput(true); // post�������Ҫ����http�����ڣ������ó�true��Ĭ����false

            conn.setDoInput(true); // �����Ƿ��httpUrlConnection���룬Ĭ���������true

            conn.setUseCaches(false); // Post ������ʹ�û���

            // �趨���͵����������ǿ����л���java����(����������,�ڴ������л�����ʱ,��WEB����Ĭ�ϵĲ�����������ʱ������java.io.EOFException)

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // ��������ʽ��GET/POST��
            conn.setRequestMethod(requestMethod);
            conn.connect();
            // ��outputStr��Ϊnullʱ�������д����
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // ע������ʽ
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // ����������ȡ��������
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
            // ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // ������SSLContext�����еõ�SSLSocketFactory����
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(30000); // ��������������ʱ����λ������)

            conn.setReadTimeout(30000); // ���ô�������ȡ���ݳ�ʱ����λ������)

            conn.setDoOutput(true); // post�������Ҫ����http�����ڣ������ó�true��Ĭ����false

            conn.setDoInput(true); // �����Ƿ��httpUrlConnection���룬Ĭ���������true

            conn.setUseCaches(false); // Post ������ʹ�û���

            // �趨���͵����������ǿ����л���java����(����������,�ڴ������л�����ʱ,��WEB����Ĭ�ϵĲ�����������ʱ������java.io.EOFException)

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // ��������ʽ��GET/POST��
            conn.setRequestMethod(requestMethod);
            // ��outputStr��Ϊnullʱ�������д����
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // ע������ʽ
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // ����������ȡ��������
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // �ͷ���Դ
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            log.error("���ӳ�ʱ��{}", ce);
        } catch (Exception e) {
            log.error("https�����쳣��{}", e);
        }
        return null;
    }

    public static JSONObject httpRequest2(String requestUrl, String requestMethod, String data) {

        JSONObject jsonObject = null;

        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(30000); // ��������������ʱ����λ������)

            conn.setReadTimeout(30000); // ���ô�������ȡ���ݳ�ʱ����λ������)

            conn.setDoOutput(true); // post�������Ҫ����http�����ڣ������ó�true��Ĭ����false

            conn.setDoInput(true); // �����Ƿ��httpUrlConnection���룬Ĭ���������true

            conn.setUseCaches(false); // Post ������ʹ�û���

            // �趨���͵����������ǿ����л���java����(����������,�ڴ������л�����ʱ,��WEB����Ĭ�ϵĲ�����������ʱ������java.io.EOFException)

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            // ��������ʽ��GET/POST��
            conn.setRequestMethod(requestMethod);

            conn.connect();

            // ��outputStr��Ϊnullʱ�������д����
            if (null != data) {

                OutputStream outputStream = conn.getOutputStream();
                // ע������ʽ
                outputStream.write(data.getBytes("UTF-8"));

                outputStream.close();
            }
            // ����������ȡ��������
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
     * ����΢�ŷ���������XML��
     *
     * @param request
     * @return Map<String, String>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {

        // ����������洢��HashMap��
        Map<String, String> map = new HashMap<String, String>();

        // ��request��ȡ��������
        InputStream inputStream = request.getInputStream();
        // ��ȡ������
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // �õ�xml��Ԫ��
        Element root = document.getRootElement();
        // �õ���Ԫ�ص������ӽڵ�
        List<Element> elementList = root.elements();

        // ���������ӽڵ�
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // �ͷ���Դ
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

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// ���ӳ�ʱ30��

            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // ��ȡ��ʱ30��

            httpUrlConn.connect();

            OutputStream os = httpUrlConn.getOutputStream();

            os.write(json.getBytes("UTF-8"));// �������

            InputStream is = httpUrlConn.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            String result = new String(jsonBytes, "UTF-8");

            System.out.println("���󷵻ؽ��:" + result);
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
            // �򿪺�URL֮�������
//			URLConnection conn = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(90000); //��������������ʱ����λ������)
            conn.setReadTimeout(90000); //���ô�������ȡ���ݳ�ʱ����λ������)
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
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
            log.error("���ӷ����� time out.");
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر��������������
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
            URL url = new URL(GET_URL);    // ���ַ���ת��ΪURL�����ַ
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();// ������
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
            return  sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ʧ��!");
        }
        return null;
    }

    public static void main(String[] args) {
        String address = "0x3D0ff5939FCaEb054fc69dfB6D751967dC37C096";
        String apikey = "DKX8WHU2W176IS44TNGG76ZK9X4WPG5IGR";
       String url =  "https://api.etherscan.io/api?module=account&action=balance&address="+address+"&tag=latest"+"&apikey="+ apikey;

        httpURLConectionGET(url);

       // interfaceUtil("http://api.map.baidu.com/telematics/v3/weather?location=����&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ", "");
//        interfaceUtil("http://192.168.10.89:8080/eoffice-restful/resources/sys/oadata", "usercode=10012");
//        interfaceUtil("http://192.168.10.89:8080/eoffice-restful/resources/sys/oaholiday",
//                    "floor=first&year=2017&month=9&isLeader=N");

    }
}
