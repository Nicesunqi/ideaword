package com.graphics.common.utils;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

public class DeviceUtils {

    /**
     * 判断是否是微信访问
     * 
     * @param request
     * @return
     */
    public static boolean isWeChat(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        return userAgent == null || userAgent.indexOf("micromessenger") == -1 ? false : true;
    }
    
    public static boolean isMobile(HttpServletRequest request) {
    	UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("StudentUser-Agent"));
		boolean isMobile = DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType());
		return isMobile;
    }
    
}
