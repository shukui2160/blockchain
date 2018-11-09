package com.tlabs.blockchain.utils;

import java.io.FileReader;
import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;


/**
 * Created by admin on 2018/8/28.
 */
public class RedisCache {
    private static final Logger LOG = Logger.getLogger(RedisCache.class);
    private static final Properties properties = PropertiesUtil.getProperties("config.properties");
    private static String luaLocation = properties.getProperty("luaLocation");
    /**----------------------------------��������----------start-----------------------**/
    //�����˺�CaiWuWuChamail
    public static String CaiWuWuChamail = properties.getProperty("CaiWuWuChamail");
    // Redis������IP
    private static String ADDR = properties.getProperty("redis_cache_host");
    //Redis�Ķ˿ں�
    private static int PORT = Integer.parseInt(properties.getProperty("redis_cache_port"));
    //��������
//	private static String AUTH = null;
    private static String AUTH = properties.getProperty("redis_cache_auth");

    // ��������ʵ���������Ŀ��Ĭ��ֵΪ8��
    // �����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��
    private static int MAX_TOTAL = Integer.parseInt(properties.getProperty("redis_max_total"));

    // ����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����Ĭ��ֵҲ��8�� ���ӳ��������е�������
    private static int MAX_IDLE = Integer.parseInt(properties.getProperty("redis_max_idle"));

    // �ȴ��������ӵ����ʱ�䣬��λ���룬Ĭ��ֵΪ-1����ʾ������ʱ����������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��
    private static int MAX_WAIT = Integer.parseInt(properties.getProperty("redis_max_wait"));

    private static int TIMEOUT = Integer.parseInt(properties.getProperty("redis_timeout"));

    private static int DATABASE = Integer.parseInt(properties.getProperty("redis_cache_datebase"));

    // ��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
    private static boolean TEST_ON_BORROW = false;

    private static JedisPool jedisPool = null;

    public static void main(String[] args) {
        // ���ӱ��ص� Redis ����
        Jedis jedis = new Jedis("172.18.1.185");
        System.out.println("���ӳɹ�");
        // �鿴�����Ƿ�����
        System.out.println("������������: " + jedis.ping());

        //ʹ���ַ���list��ֵ
        jedis.lpush("����", "�Ͼ�");
        jedis.lpush("����", "�Ϻ�");
        jedis.lpush("����", "����");
        jedis.lpush("����", "����");
        jedis.lpush("����", "��ͨ");


        //list����ȡֵ,����ע�����,100��λ���ǽ����ĽǱ�,�������û��,С�˵Ļ��ͻ�ȱ
        List<String> arr = jedis.lrange("����", 0, 100);
        System.out.println(arr.size());
        for (String string : arr) {
            System.out.println(string);
        }

        //Redis hash ��һ��string���͵�field��value��ӳ���hash�ر��ʺ����ڴ洢����
        //����Ҫ�����map������key��value����string���͵�
        Map<String, String> map=new HashMap<>();
        map.put("name", "С��");
        map.put("age", "13");
        map.put("sex", "��");
        map.put("height", "174");

        //����jedis��hmset(����hash map)�ķ�����map�ļ�ֵ�Դ��ȥ
        jedis.hmset("people", map);


    }


    /**
     * ��ʼ��Redis���ӳ�
     */
    private static void initialPool() {
        try {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setMaxTotal(MAX_TOTAL);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestOnReturn(false);
            config.setTestWhileIdle(false);
            config.setBlockWhenExhausted(true);
            config.setLifo(false);

            config.setSoftMinEvictableIdleTimeMillis(120000);

            if(AUTH != null && AUTH.equals("")){
                AUTH = null;
            }

            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT,null,DATABASE);//����
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT,AUTH,DATABASE);//����
            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);//����
            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, null, DATABASE);			//155
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �ڶ��̻߳���ͬ����ʼ��
     */
    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }

    /**
     * ��ȡJedisʵ��
     *
     * @return
     */
    private synchronized static Jedis getJedis() {
        Jedis jedis = null;
        try {
            if (jedisPool == null) {
                poolInit();
            }
            if (jedisPool != null) {
                jedis = jedisPool.getResource();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return jedis;
        }
        return jedis;
    }

    /**
     * �ͷ�jedis��Դ
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {

        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * ���� ����ʱ��
     *
     * @param key
     * @param seconds
     *            ����Ϊ��λ
     * @param value
     */
    public static String setString(String key, String value, int seconds) {
        Jedis jedis = getJedis();
        String r = value = StringUtil.isNotEmpty(value) ? value : "";
        jedis.setex(key, seconds, value);
        returnResource(jedis);
        return r;

    }

    /**
     * ���� (����)
     *
     * @param key
     *            ����Ϊ��λ
     * @param value
     */
    public static boolean setStringIfNotExist(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }

        String valueNew = value;
        if (StringUtils.isBlank(value)) {
            valueNew = "";
        }

        Jedis jedis = getJedis();
        if (jedis == null) {
            return false;
        }

        long ret = jedis.setnx(key, valueNew);
        returnResource(jedis);

        // success
        if (ret == 1) {
            return true;
        }
        // failed
        return false;
    }


    /**
     * ���� ����ʱ��
     *
     * @param key
     *            ����Ϊ��λ
     * @param value
     */
    public static String setString(String key, String value) {
        Jedis jedis = getJedis();
        String r = value = StringUtil.isNotEmpty(value) ? value : "";
        if (jedis.exists(key)) {
            jedis.del(key);
        }
        jedis.set(key, value);
        returnResource(jedis);
        return r;

    }

    public static void setExpiredTime(String key,Integer seconds){
        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        returnResource(jedis);
    }

    public static Long queryRemainingTimes(String key){
        Jedis jedis = getJedis();
        Long ttl = jedis.ttl(key);
        returnResource(jedis);
        return ttl;
    }


    /**
     * ��ȡStringֵ
     *
     * @param key
     * @return value
     */
    public static String getString(String key) {
        Jedis jedis = getJedis();
        if (jedis == null || !jedis.exists(key)) {
            returnResource(jedis);
            return null;
        }
        String value = jedis.get(key);
        returnResource(jedis);
        if (null == value || "".equals(value)) {

            return null;
        }
        return value;
    }

    /**
     * ���������redis
     *
     * @param key
     *            ����
     * @param object
     *            ����
     */
    public static <T extends Serializable> void writeObject(String key, T object) {
        Jedis jedis = getJedis();
        if(null!=object){
            jedis.set(key.getBytes(), SerializeUtil.serialize(object));
        }
        returnResource(jedis);
    }


    //���֧�ִ漯��
    public static void writeObject ( String key , Object object ){
        Jedis jedis = getJedis ( );
        if(null!=object){
            jedis.set (key.getBytes() , SerializeUtil.serialize(object));
        }
        returnResource ( jedis );
    }

    public static boolean isExistsScript(String hash){
        Jedis jedis = getJedis ( );
        boolean test = jedis.scriptExists(hash);
        returnResource ( jedis );
        return test;
    }
    /**
     * ���������redis�������ù���ʱ��
     *
     * @param key
     *            ����
     * @param seconds
     *            ��������
     * @param object
     *            �������
     * @return
     */
    public static <T extends Serializable> void writeObject(String key, T object, int seconds) {
        Jedis jedis = getJedis();
        if(null!=object){
            if (seconds <= 0) {
                jedis.set(key.getBytes(), SerializeUtil.serialize(object));
            } else {
                jedis.setex(key.getBytes(), seconds, SerializeUtil.serialize(object));
            }
        }

        returnResource(jedis);
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> T getObject(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        byte[] content = jedis.get((key).getBytes());
        returnResource(jedis);
        if(null==content||content.length==0){
            return null;
        }
        T t = (T) SerializeUtil.unserialize(content);
        return t;
    }

    /**
     * ��redis��ȡ����
     * @param key
     * @return
     */
    public static Object getObject ( String key ){
        Jedis jedis = getJedis ( );
        byte [ ] content = jedis.get ( ( key ).getBytes ( ) );
        returnResource ( jedis );
        if(null==content||content.length==0){
            return null;
        }
        return SerializeUtil.unserialize ( content );
    }

    /**
     * ���������redis�������ù���ʱ��
     *
     * @param key
     *            ����
     * @param seconds
     *            ��������
     * @param object
     *            �������
     * @return
     */
    public static <T extends Serializable> void writeObjectS(String key, T object, int seconds) {
        Jedis jedis = getJedis();
        String value = SerializeUtil.serializeBase64(object);
        if (seconds <= 0) {
            jedis.set(key, value);
        } else {
            jedis.setex(key, seconds, value);
        }
        returnResource(jedis);
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> T getObjectS(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        String content = jedis.get(key);
        returnResource(jedis);
        if(null==content||"".equals(content)){
            return null;
        }
        T t = (T) SerializeUtil.unserializeBase64(content);
        return t;
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> void rpush(String key, T[] values, int seconds) {
        if (values == null || values.length == 0) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            String[] arr = new String[values.length];
            int i = 0;
            for (T t : values) {
                String value = SerializeUtil.serializeBase64(t);
                arr[i] = value;
                i++;
            }
            jedis.rpush(key, arr);
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }


    public static <T extends Serializable> void rpush(String key, Object values) {

        Jedis jedis = getJedis();
        try {
            String value = SerializeUtil.serializeBase64(values);
            jedis.rpush(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }


    public static <T extends Serializable> void lpop(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.lpop(key);
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    public static long listLength(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.llen(key);
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }
    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static void rpushString(String key, String[] values, int seconds) {
        if (values == null || values.length == 0) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.rpush(key, values);
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static void lpushString(String key, String[] values, int seconds) {
        if (values == null || values.length == 0) {
            return;
        }
        Jedis jedis = getJedis();

        try {
            jedis.lpush(key, values);
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> List<T> lrange(String key, int a, int b, Class<T> clazz) {
        Jedis jedis = getJedis();
        try {
            List<String> values = jedis.lrange(key, a, b);
            if (values == null || values.size() == 0) {
                return null;
            } else {
                List<T> list = new ArrayList<T>();
                for (String str : values) {
                    T t = (T) SerializeUtil.unserializeBase64(str);
                    list.add(t);
                }
                return list;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static List<String> lrange(String key, int a, int b) {
        Jedis jedis = getJedis();
        try {
            List<String> values = jedis.lrange(key, a, b);
            if (values == null || values.size() == 0) {
                return null;
            } else {
                return values;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static void hsetString(String key, String field, String value, int seconds) {
        if (value == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            jedis.hset(key, field, value);
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> void hset(String key, String field, T t, int seconds) {
        if (t == null) {
            return;
        }
        Jedis jedis = getJedis();
        try {
            String value = SerializeUtil.serializeBase64(t);
            jedis.hset(key, field, value);
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> T hget(String key, String field, Class<T> clazz) {
        Jedis jedis = getJedis();
        try {
            String value = jedis.hget(key, field);
            if (value == null || "".equals(value)) {
                return null;
            } else {
                T t = (T) SerializeUtil.unserializeBase64(value);
                return t;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static String hget(String key, String field) {
        Jedis jedis = getJedis();
        try {
            String value = jedis.hget(key, field);
            if (value == null || "".equals(value)) {
                return null;
            } else {
                return value;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> List<T> hgetAll(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        try {
            List<T> list = null;
            Set<String> fields = jedis.hkeys(key);
            if (fields != null) {
                list = new ArrayList<T>();
                for (String field : fields) {
                    String value = jedis.hget(key, field);
                    T t = (T) SerializeUtil.unserializeBase64(value);
                    list.add(t);
                }
            }
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static List<String> hgetAll(String key) {
        Jedis jedis = getJedis();
        try {
            List<String> list = null;
            Set<String> fields = jedis.hkeys(key);
            if (fields != null) {
                list = new ArrayList<String>();
                for (String field : fields) {
                    String value = jedis.hget(key, field);
                    list.add(value);
                }
            }
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @param key
     * @return
     */
    public static Set<String> hkeys(String key) {
        Jedis jedis = getJedis();
        try {
            Set<String> set = jedis.hkeys(key);
            return set;
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ����ض�����redis���Ƿ����
     *
     * @param key
     * @return
     */
    public static boolean hexists(String key, String field) {
        Jedis jedis = getJedis();
        if (jedis.hexists(key, field)) {
            returnResource(jedis);
            return true;
        }
        returnResource(jedis);
        return false;
    }

    /**
     * ɾ���ض�redis��
     *
     * @param key
     */
    public static void hdel(String key, String field) {
        Jedis jedis = getJedis();
        jedis.hdel(key, field);
        returnResource(jedis);
    }

    /**
     * ��redis��ȡ����
     *
     * @param key
     * @return
     */
    public static Set<String> zrangeByScore(String key, String min, String max) {
        Jedis jedis = getJedis();
        try {
            Set<String> set = jedis.zrangeByScore(key, min, max);
            return set;
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    public static void zrem(String key, String... members){
        Jedis jedis = getJedis();
        try {
            jedis.zrem(key, members);
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }
    public static void zremrangeByRank(String key, int start,int end){
        Jedis jedis = getJedis();
        try {
            jedis.zremrangeByRank(key,start,end);
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    public static void zadd(String key, double score, String member){
        Jedis jedis = getJedis();
        try {
            jedis.zadd(key, score, member);
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }
    /**
     * @param key
     * @return
     */
    public static Set<String> keys(String key) {
        Jedis jedis = getJedis();
        try {
            Set<String> set = jedis.keys(key);
            return set;
        } catch (Exception e) {
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ����ض�����redis���Ƿ����
     *
     * @param key
     * @return
     */
    public static void expire(String key, int seconds) {
        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        returnResource(jedis);
    }

    /**
     * ����ض�����redis���Ƿ����
     *
     * @param key
     * @return
     */
    public static boolean isExists(String key) {
        Jedis jedis = getJedis();
        if (jedis.exists(key.getBytes())) {
            returnResource(jedis);
            return true;
        }
        returnResource(jedis);
        return false;
    }

    /**
     * ɾ���ض�redis��
     *
     * @param key
     */
    public static void delObject(String key) {
        Jedis jedis = getJedis();
        jedis.del((key).getBytes());
        returnResource(jedis);
    }
    public static void del(String key) {
        Jedis jedis = getJedis();
        jedis.del(key);
        returnResource(jedis);
    }
    public static String checkConn() {
        int a = jedisPool.getNumActive();
        int b = jedisPool.getNumIdle();
        int c = jedisPool.getNumWaiters();
        return a + "," + b + "," + c;
    }


    public static String loadScript(String fileName) {
        Jedis jedis = getJedis();
        try {
            FileReader fr = new FileReader(luaLocation+fileName);
            StringBuffer buffer = new StringBuffer();
            int c = 0;
            while((c=fr.read())!=-1){
                buffer.append((char)c);
            }
            fr.close();
            String hash = jedis.scriptLoad(buffer.toString());
            returnResource(jedis);
            return hash;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }



    /**
     * �� sortedSet�Ĳ���
     */
    public static void ZADD(String key,double score,String member) {
        Jedis jedis = getJedis();
        jedis.zadd(key, score, member);
        returnResource(jedis);
    }


    public static Long ZCARD(String key) {
        Jedis jedis = getJedis();
        Long count = jedis.zcard(key);
        returnResource(jedis);
        return count>0l?count:0;
    }


    public static void BatchZADD(String key,Map<String, Double> sourceMember) {
        Jedis jedis = getJedis();
        jedis.zadd(key, sourceMember);
        returnResource(jedis);
    }

    public static void sadd(String key,String value) {
        Jedis jedis = getJedis();
        jedis.sadd(key, value);
        returnResource(jedis);
    }
    public static void sdel(String key,String value) {
        Jedis jedis = getJedis();
        jedis.srem(key, value);
        returnResource(jedis);
    }


    public static Set<Tuple> ZRANGEAll(String key) {
        Jedis jedis = getJedis();
        Set<Tuple> tuple = jedis.zrangeWithScores(key, 0, -1);
        returnResource(jedis);
        return tuple.size()>0?tuple:null;
    }

    /**
     * page ҳ��
     * pagesize ÿҳ����
     * sort	����   0   ���� score��С����      1 ����   score�Ӵ�С
     * @param key
     * @param page
     * @param pagesize
     * @param sort
     * @return
     */
    public static Set<Tuple> ZRANGE(String key,int page,int pagesize,int sort) {
        //����
        if(page==0){
            //��ֹ����0
            page=1;
        }
        if(pagesize==0){
            pagesize=1;
        }
        Jedis jedis = getJedis();
        //��ȡ����
        Long count = jedis.zcard(key);
        Set<Tuple> tuple = null;
        if(sort==0){
            Long pageL=new Long(page);
            Long pagesizeL=new Long(pagesize);
            Long startIndex=((pageL-1)*pagesizeL);
            Long stopIndex=(pageL*pagesizeL-1l);
            tuple=jedis.zrangeWithScores(key,startIndex,stopIndex);
        }else if(sort==1){
            //����
            Long pageL=new Long(page);
            Long pagesizeL=new Long(pagesize);
            Long startIndex=((pageL-1)*pagesizeL);
            Long stopIndex=(pageL*pagesizeL-1l);
            tuple= jedis.zrevrangeWithScores(key,startIndex,stopIndex);
        }else{
            //����
        }
        returnResource(jedis);
        return tuple.size()>0?tuple:null;
    }
    public static byte[] getByte(String key) {
        Jedis jedis = getJedis();
        byte[] bs = jedis.get(key.getBytes());
        returnResource(jedis);
        return null!=bs&&bs.length>0l?bs:null;
    }


    public static void writeByte(String key,byte[] value,int seconds) {
        Jedis jedis = getJedis();
        if(null!=value&&value.length>0){
            if (seconds <= 0) {
                jedis.set(key.getBytes(), value);
            } else {
                jedis.setex(key.getBytes(), seconds, value);
            }
        }

        returnResource(jedis);
    }

    public static Object executeScript(String hash,List<String> keys,List<String> args){
        Jedis jedis = getJedis();
        LOG.info(keys.toString());
        LOG.info(args.toString());

        Object o = jedis.evalsha(hash,keys,args);
        returnResource(jedis);
        return o;
    }
    public static void delAllKeys(){
        Jedis jedis = getJedis();
        jedis.flushDB();
        returnResource(jedis);
    }

    public static Object evalSha(String script, int keyCount, String...params){
        Jedis jedis = getJedis();
        LOG.info(script);
        for(int i=0;i<params.length;i++){
            LOG.info(params[i]);
        }
        Object o = jedis.evalsha(script, keyCount, params);
        returnResource(jedis);
        return o;
    }
    public static Object lindex(String key, int index){
        Jedis jedis = getJedis();
        String o = jedis.lindex(key, index);
        returnResource(jedis);
        return o;
    }

    public static void setAddressPort(String address,int port,String auth){
        ADDR = address;
        PORT = port;
        AUTH = auth;
    }
    public static boolean existsScript(String hash){
        Jedis jedis = getJedis();
        boolean o = jedis.scriptExists(hash);
        returnResource(jedis);
        return o;
    }
    public static void setDatabase(int database){
        DATABASE = database;
    }
}
