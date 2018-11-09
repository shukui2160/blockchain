package com.tlabs.blockchain.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by admin on 2018/7/25.
 */
public class PropertiesUtil {
    private static Logger log = Logger.getLogger(PropertiesUtil.class);

    public static Properties getProperties(String propertiesName) {

        Properties p = new Properties();

        try {
            InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            p.load(inputStream);

        }
        catch (IOException e) {
            log.error("读取配置文件{" + propertiesName + "}出现异常");
            throw new RuntimeException(e);
        }

        return p;
    }

    public static void main(String[] args){
        try{
            Properties p = getProperties("message.properties");
            for (Object key : p.keySet() )
            {
                System.out.println("key:"+key);
                System.out.println(p.getProperty(key.toString()));
//            		System.out.println(ss[1]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
