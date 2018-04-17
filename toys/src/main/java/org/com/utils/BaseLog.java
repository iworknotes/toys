package org.com.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author tianxu
 */
public abstract class BaseLog {

    /**
     * 通用的logger
     */
    protected final Log logger = LogFactory.getLog(getClass());
}