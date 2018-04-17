package org.com.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description: Property工具类
 * @author binary
 */
public class PropertyUtil {

    private static final String SUFFIX = ".properties";

    private final static Logger logger = LoggerFactory.getLogger(Thread.currentThread().getClass());

    /**
     * 
     * @MethodName: getPropertyValue
     * @Description: 从/WEB-INF/路径下读取给定的filename文件中的内容
     * @param filename /WEB-INF/路径下的properties文件名
     * @param key
     * @return
     */
    public static String getPropertyValue(String filename, String key) {
        if (StringUtils.isNotBlank(key)) {
            if (StringUtils.isBlank(filename))
                filename = "config.properties";
            if (!filename.endsWith(SUFFIX))
                filename = filename + SUFFIX;
            Properties properties = new Properties();
            try (InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + filename);
                    InputStreamReader isr = new InputStreamReader(ins, "UTF-8");) {
                properties.load(isr);
                return properties.getProperty(key);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return "";
    }

    /**
     * 
     * @MethodName: getPropertyValue
     * @Description: 默认从/WEB-INF/config.properties文件中读取配置
     * @param key
     * @return
     */
    public static String getPropertyValue(String key) {
        return getPropertyValue("", key);
    }
}
