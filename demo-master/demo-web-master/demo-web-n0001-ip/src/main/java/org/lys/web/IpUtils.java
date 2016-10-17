package org.lys.web;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {
    private IpUtils() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    
    public static boolean hasMatch(String clientAddr, String[] regExps) {
        for (int i=0; i < regExps.length; i++) {
            if (clientAddr.matches(regExps[i])) return true;
        }
        
        return false;
    }
    
    public static String[] extractRegExps(String initParam) {  
        if (initParam == null) {
            return new String[0];
        } else {
            return initParam.split(",");
        }
    }
    
    public static void main(String[] args) {
		String[] deny = extractRegExps("58.53.39.*,192.4.5.7");
		String[] allow = extractRegExps("10.128.9.*,127.0.0.*");
		
		String clientAddr="10.128.8.6";
		if (hasMatch(clientAddr, deny)) {
			System.out.println("拒绝IP");
            return;
        }
        
        if ((allow.length > 0) && !hasMatch(clientAddr, allow)) {
        	System.out.println("不在通过ip中");
            return;
        }
        System.out.println("放行");
	}
    
}
