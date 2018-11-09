package com.tlabs.blockchain.utils;

import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/28.
 */
public class SerializeUtil {
    private static final Logger LOG = Logger.getLogger(SerializeUtil.class);
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // ���л�
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }finally{
            if(oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            // �����л�
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }finally{
            if(bais!=null){
                try {
                    bais.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }if(ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String serializeBase64(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // ���л�
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return new BASE64Encoder().encode(baos.toByteArray());
        } catch (Exception e) {

        }finally{
            if(oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object unserializeBase64(String str) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            // �����л�
            bais = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(str));
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }finally{
            if(bais!=null){
                try {
                    bais.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }if(ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static void main(String[] args){

    }

    /**
     * �رյ�����Դ��Ŀ�ꡣ���� close()�������ͷŶ��󱣴����Դ������ļ���
     * �رմ������ͷ����������������ϵͳ��Դ������Ѿ��رո���������ô˷�����Ч��
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LOG.info("Unable to close %s", e);
            }
        }
    }

    /**
     *
     * @param value
     * @return
     */
    public static <T> byte[] serialize(List<T> value) {
        if (value == null) {
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv=null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            for(T obj : value){
                os.writeObject(obj);
            }
            os.writeObject(null);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object", e);
        } finally {
            close(os);
            close(bos);
        }
        return rv;
    }



    /**
     * �����л��б�����Redis������ȡ��
     * @param in
     * @return
     */
    public static <T> List<T> unserializeForList(byte[] in) {
        List<T> list = new ArrayList<T>();
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if(in != null) {
                bis=new ByteArrayInputStream(in);
                is=new ObjectInputStream(bis);
                while (true) {
                    T obj = (T) is.readObject();
                    if(obj == null){
                        break;
                    }else{
                        list.add(obj);
                    }
                }
                is.close();
                bis.close();
            }
        } catch (IOException e) {
            LOG.info("Caught IOException decoding %d bytes of data", e);
        } catch (ClassNotFoundException e) {
            LOG.warn("Caught CNFE decoding %d bytes of data", e);
        } finally {
            close(is);
            close(bis);
        }
        return list;
    }
}
