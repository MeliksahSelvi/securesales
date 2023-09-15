package com.meliksah.securesales.utils;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import static com.meliksah.securesales.constants.Constants.USER_AGENT_HEADER;
import static com.meliksah.securesales.constants.Constants.X_FORWARDED_FOR_HEADER;
import static nl.basjes.parse.useragent.UserAgent.AGENT_NAME;
import static nl.basjes.parse.useragent.UserAgent.DEVICE_NAME;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

public class RequestUtils {



    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = "Unknown IP";
        if (request != null) {
            ipAddress = request.getHeader(X_FORWARDED_FOR_HEADER);
            if (ipAddress == null || "".equals(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        }

        return ipAddress;
    }

    public static String getDevice(HttpServletRequest request) {
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(1000).build();
        UserAgent userAgent = userAgentAnalyzer.parse(request.getHeader(USER_AGENT_HEADER));
//        return userAgent.getValue(OPERATING_SYSTEM_NAME) + "-" + userAgent.getValue(AGENT_NAME) + " - " + userAgent.getValue(DEVICE_NAME);
        return userAgent.getValue(AGENT_NAME) + " - " + userAgent.getValue(DEVICE_NAME);
    }
}
