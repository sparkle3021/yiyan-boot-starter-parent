package com.yiyan.boot.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 */
public class IpUtils {

    /**
     * 获取请求的IP地址
     *
     * @param request 请求信息
     * @return IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        // 如果有多个代理服务器，X-Forwarded-For会包含多个IP，取第一个即为真实IP
        if (clientIp != null && clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }

        return clientIp;
    }
}
