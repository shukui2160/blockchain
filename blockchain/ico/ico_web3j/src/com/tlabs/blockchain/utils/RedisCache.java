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
    /**----------------------------------开发环境----------start-----------------------**/
    //邮箱账号CaiWuWuChamail
    public static String CaiWuWuChamail = properties.getProperty("CaiWuWuChamail");
    // Redis服务器IP
    private static String ADDR = properties.getProperty("redis_cache_host");
    //Redis的端口号
    private static int PORT = Integer.parseInt(properties.getProperty("redis_cache_port"));
    //访问密码
//	private static String AUTH = null;
    private static String AUTH = properties.getProperty("redis_cache_auth");

    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL = Integer.parseInt(properties.getProperty("redis_max_total"));

    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。 连接池中最大空闲的连接数
    private static int MAX_IDLE = Integer.parseInt(properties.getProperty("redis_max_idle"));

    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = Integer.parseInt(properties.getProperty("redis_max_wait"));

    private static int TIMEOUT = Integer.parseInt(properties.getProperty("redis_timeout"));

    private static int DATABASE = Integer.parseInt(properties.getProperty("redis_cache_datebase"));

    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = false;

    private static JedisPool jedisPool = null;

    public static void main(String[] args) {
        // 连接本地的 Redis 服务
        Jedis jedis = new Jedis("172.18.1.185");
        System.out.println("连接成功");
        // 查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());

        //使用字符串list存值
        jedis.lpush("城市", "南京");
        jedis.lpush("城市", "上海");
        jedis.lpush("城市", "苏州");
        jedis.lpush("城市", "北京");
        jedis.lpush("城市", "南通");


        //list集合取值,这里注意的是,100的位置是结束的角标,如果大了没事,小了的话就会缺
        List<String> arr = jedis.lrange("城市", 0, 100);
        System.out.println(arr.size());
        for (String string : arr) {
            System.out.println(string);
        }

        //Redis hash 是一个string类型的field和value的映射表，hash特别适合用于存储对象。
        //这里要求的是map必须是key和value都是string类型的
        Map<String, String> map=new HashMap<>();
        map.put("name", "小明");
        map.put("age", "13");
        map.put("sex", "男");
        map.put("height", "174");

        //调用jedis的hmset(存入hash map)的方法将map的键值对存进去
        jedis.hmset("people", map);


    }


    /**
     * 初始化Redis连接池
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

            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT,null,DATABASE);//开发
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT,AUTH,DATABASE);//测试
            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);//本地
            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, null, DATABASE);			//155
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }

    /**
     * 获取Jedis实例
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
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {

        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 设置 过期时间
     *
     * @param key
     * @param seconds
     *            以秒为单位
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
     * 设置 (互斥)
     *
     * @param key
     *            以秒为单位
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
     * 设置 过期时间
     *
     * @param key
     *            以秒为单位
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
     * 获取String值
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
     * 将对象存入redis
     *
     * @param key
     *            键名
     * @param object
     *            对象
     */
    public static <T extends Serializable> void writeObject(String key, T object) {
        Jedis jedis = getJedis();
        if(null!=object){
            jedis.set(key.getBytes(), SerializeUtil.serialize(object));
        }
        returnResource(jedis);
    }


    //这个支持存集合
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
     * 将对象存入redis，并设置过期时间
     *
     * @param key
     *            键名
     * @param seconds
     *            过期秒数
     * @param object
     *            待存对象
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 将对象存入redis，并设置过期时间
     *
     * @param key
     *            键名
     * @param seconds
     *            过期秒数
     * @param object
     *            待存对象
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 从redis中取数据
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
     * 检查特定键在redis中是否存在
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
     * 删除特定redis键
     *
     * @param key
     */
    public static void hdel(String key, String field) {
        Jedis jedis = getJedis();
        jedis.hdel(key, field);
        returnResource(jedis);
    }

    /**
     * 从redis中取数据
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
     * 检查特定键在redis中是否存在
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
     * 检查特定键在redis中是否存在
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
     * 删除特定redis键
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
     * 对 sortedSet的操作
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
     * page 页数
     * pagesize 每页数量
     * sort	排序   0   正序 score从小到大      1 倒叙   score从大到小
     * @param key
     * @param page
     * @param pagesize
     * @param sort
     * @return
     */
    public static Set<Tuple> ZRANGE(String key,int page,int pagesize,int sort) {
        //正序
        if(page==0){
            //防止出现0
            page=1;
        }
        if(pagesize==0){
            pagesize=1;
        }
        Jedis jedis = getJedis();
        //获取总数
        Long count = jedis.zcard(key);
        Set<Tuple> tuple = null;
        if(sort==0){
            Long pageL=new Long(page);
            Long pagesizeL=new Long(pagesize);
            Long startIndex=((pageL-1)*pagesizeL);
            Long stopIndex=(pageL*pagesizeL-1l);
            tuple=jedis.zrangeWithScores(key,startIndex,stopIndex);
        }else if(sort==1){
            //倒叙
            Long pageL=new Long(page);
            Long pagesizeL=new Long(pagesize);
            Long startIndex=((pageL-1)*pagesizeL);
            Long stopIndex=(pageL*pagesizeL-1l);
            tuple= jedis.zrevrangeWithScores(key,startIndex,stopIndex);
        }else{
            //倒叙
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
