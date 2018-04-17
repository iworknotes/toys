package org.com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description: 读取项目中/WEB-INF/config.properties文件
 * @author binary
 */
public enum PropertiesConfig {

    INSTANCE;

    private volatile Properties configuration = new Properties();
    private final String LOAD_FILE_NAME = "config.properties";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PropertiesConfig() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + LOAD_FILE_NAME);
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");) {
            if (null != is) {
                this.configuration.clear();
                this.configuration.load(isr);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String getValue(String key) {
        return this.configuration.getProperty(key);
    }

}
