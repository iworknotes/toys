package org.com.utils;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * spymemcached客户端
 *
 */
public class MyStorage {

    private static final Logger logger = Logger.getLogger(MyStorage.class);

    /**
     * 所有方法共用一个客户端实例
     * 生产环境下，100w DAU，2000w用户
     */
    private static MemcachedClient c = null;

    public static final int save_time_default = 86400 * 3;
    public static final int save_token_time = 86400 * 1;

    public static final int MINUTE_5 = 5 * 60;
    public static final int MINUTE_30 = 1800; // 30 * 60
    public static final int ONE_HOUR = 3600;
    public static final int DAY_5 = 86400 * 5;
    public static final int DAY_15 = 86400 * 15;
    public static final int DAY_30 = 86400 * 30;

    public static final String status_key = "status_";
    public static int DEFAULT_TIMEOUT = 5;
    public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;

    static {
        try {
            c = new MemcachedClient(new BinaryConnectionFactory(),
                    AddrUtil.getAddresses(PropertyUtil.getPropertyValue("config.properties", "memcached.address")));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static boolean save(String key, Object value, int seconds) {
        Future<Boolean> f = c.set(key, seconds, value);
        return getBooleanValue(f);
    }

    public static boolean append(String key, Object value) {
        Future<Boolean> f = c.append(key, value);
        return getBooleanValue(f);
    }

    public static boolean save(String key, Object value) {
        return save(key, value, save_time_default);
    }

    public static boolean saveOneDay(String key, Object value) {
        return save(key, value, save_token_time);
    }

    public static void remove(String key) {
        c.delete(key);
    }

    public static Object get(String key) {
        logger.debug("get memcached key:" + key);
        return c.get(key);
    }

    private static boolean getBooleanValue(Future<Boolean> f) {
        try {
            Boolean bool = f.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
            return bool.booleanValue();
        } catch (Exception e) {
            f.cancel(false);
            return false;
        }
    }

}