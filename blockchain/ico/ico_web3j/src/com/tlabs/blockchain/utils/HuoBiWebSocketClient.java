package com.tlabs.blockchain.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * Created by admin on 2018/7/25.
 */
public class HuoBiWebSocketClient {
    private static final Logger log = Logger.getLogger(HuoBiWebSocketClient.class);

    private static Session session = null;
    public boolean openOrClose = true;

    private static Properties properties = PropertiesUtil.getProperties("config.properties");

    private static DecimalFormat df = new DecimalFormat("#.########");

    private  static String URL = properties.getProperty("huobi_websocket_url");
    private static Map<String,Long> subMap = new HashMap();
    public static final int STATUS_IDLE = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_STOPPED = 3;
    private int status = STATUS_IDLE;
    //private static String URL = "wss://api.huobipro.com/ws";
    //private String URL = "ws://demos.kaazing.com/echo";
    //private static String URL = "ws://103.242.67.140:90/ws";

    private static Map<String, String> priceMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        //log.info("connection established");
    }

    @OnMessage
    public void onMessage(String message) {
        //log.info("receive: " + message);
    }

    @OnMessage
    public void binaryMessage(Session session, ByteBuffer msg) {

        try {
//			log.info("收到消息");
            String message = uncompress(msg);
//			log.info("消息内容:"+message);
            if (message.contains("ping")) {
                session.getAsyncRemote().sendText(message.replace("ping", "pong"));
            } else {
                try {
                    //message =  "{\"ch\":\"market.btcusdt.detail\",\"ts\":1526646036241,\"tick\":{\"amount\":17476.888953486387801209,\"open\":8325.790000000000000000,\"close\":8105.010000000000000000,\"high\":8364.300000000000000000,\"id\":7584201635,\"count\":129876,\"low\":7892.530000000000000000,\"version\":7584201635,\"vol\":141765018.941610193687222239730000000000000000}}";
                    JSONObject jo = JSONObject.fromObject(message);
                    if(!jo.containsKey("ch")){
                        return ;
                    }
                    String ch = jo.getString("ch").toLowerCase();
                    if(!ch.startsWith("market")||!ch.endsWith("detail")||ch.contains("trade")){
                        return ;
                    }
                    //String price = jo.getJSONObject("tick").getString("high");

                    String price = df.format(jo.getJSONObject("tick").get("close"));
                    //System.out.println("price:-------"+price);
                    if(price!=null){
                        priceMap.put(ch.substring(7,ch.lastIndexOf(".")), price);
                        subMap.put(ch.substring(7,ch.lastIndexOf(".")), System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnMessage
    public void pongMessage(Session session, PongMessage msg) {
        byte[] b = new byte[1024];
        msg.getApplicationData().get(b);
    }

    @OnClose
    public void onClose() {
		/*session = null;
		//log.info("connection closed");
		startWork ();
		//log.info("connection closed");
		Set<String> set = priceMap.keySet();
		for(String key :set){
			subscribePriceChange(key);
		}*/
    }

    public static String uncompress(ByteBuffer str) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(str.array()));
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
        return out.toString();
    }

    public static String getPrice (String tpId) {
        //log.info("session目前状态"+session+"-------"+session.isOpen()+"--------");
        if(session==null||!session.isOpen()){
            startWork ();
            //log.info("connection closed");
            Set<String> set = priceMap.keySet();
            for(String key :set){
                subscribePriceChange(key);
            }
        }
        tpId = tpId.replace("/", "").toLowerCase();
        if(tpId.equals("tnbusdt")){
            try{
                return String.valueOf(Double.parseDouble(priceMap.get("tnbbtc"))*Double.parseDouble(priceMap.get("btcusdt")));
            }catch(Exception e){
                log.info("tnb/usdt价格换算失败");
            }
        }
        if(subMap.get(tpId)!=null&&System.currentTimeMillis()-subMap.get(tpId)>40000){
            subscribePriceChange(tpId);
            priceMap.put(tpId, null);
            subMap.put(tpId, System.currentTimeMillis());
        }
        //log.info("2222222:"+priceMap.toString());
        return priceMap.get(tpId);

    }

    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
        return out.toString();
    }

    public static int subscribePriceChange (String tpTag) {
        if (session == null || !session.isOpen()) {
            startWork ();
        }

        if (session.isOpen()) {
            String tag = tpTag.toLowerCase().replace("/", "").trim();
            if(tag.equals("tnbusdt")){
                return 0;
            }
            JSONObject jo = new JSONObject ();
            jo.put("sub", "market." + tag + ".detail");
            session.getAsyncRemote().sendText(jo.toString());
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //log.info("send subscribe request TP:" + tag);
        }
        return 0;
    }

    public static int startWork () {
        if (session == null || !session.isOpen()) {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            try {
                session = container.connectToServer(HuoBiWebSocketClient.class, URI.create(URL));
            } catch (Exception e) {
                e.printStackTrace();
                log.info("启动失败，异常:" + e);
                return -1;
            }
        }
        return 0;
    }

    public static int stopWork() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.info("停止失败，异常:" + e);
                return -1;
            }
        }
        return 0;
    }


    public static void main(String[] args) throws Exception{
        //startWork();
        subscribePriceChange("btcusdt");
        subscribePriceChange("tnbbtc");
        subscribePriceChange("tnbeth");
        //subscribePriceChange("2","btcusdt");
        while(true){
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("..........."+getPrice("tnbbtc"));
        }
    }
}
