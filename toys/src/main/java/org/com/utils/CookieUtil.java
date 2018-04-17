package org.com.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CookieUtil {
    private static Logger logger= Logger.getLogger(CookieUtil.class);
    
    public static final int ONE_DAY = 86400;
    public static final int ONE_WEEK = 7 * 86400;
    public static final int ONE_MONTH = 30 * 86400;
	private static final int SIX_MONTH = 180 * 86400;

	public static String getCookie(HttpServletRequest req, String key, String domain){
		try{
			Cookie[] cookies = req.getCookies();
			if(cookies!=null) {
				for(Cookie c:cookies){
		          	c.setDomain(domain);
			       	if(c.getName().equals(key)) {
			       		logger.debug("value="+c.getValue());
			          	return c.getValue();
			       	}
			    }
			}
		}catch(Exception e){
			logger.error("getCookie",e);
		}
		return null;
	}

    /**
     * 判断 cookies 是否存在
     *
     * @param req
     * @param key
     * @param domain
     * @return
     */
    public static boolean isCookie(HttpServletRequest req, String key, String domain) {
        if (StringUtils.isBlank(CookieUtil.getCookie(req, key, domain)))
            return true;
        else
            return false;
    }

    /**
     * 默认cookie时间 半年
	 * 
	 * @param resp
	 * @param key
	 * @param value
	 * @param domain
	 */
	public static void setCookie(HttpServletResponse resp, String key, String value, String domain){
		setCookie(resp, key, value, domain, SIX_MONTH);
	}
	
	public static void setCookie(HttpServletResponse resp, String key, String value, String domain, int maxAge){
		Cookie c = new Cookie(key,value);
        c.setMaxAge(maxAge);
        c.setPath("/");
        c.setDomain(domain);
        resp.addCookie(c);
	}

	public static void delCookie(HttpServletRequest req, HttpServletResponse resp, String key, String domain){
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for(Cookie c:cookies){
		       if(c.getName().equals(key)) {
		          c.setMaxAge(0);
		          c.setPath("/");
		          c.setDomain(domain);
		          resp.addCookie(c);
		       }
		    }
		}
	}
}