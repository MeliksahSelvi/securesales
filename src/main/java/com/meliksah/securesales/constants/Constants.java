package com.meliksah.securesales.constants;

/**
 * @Author mselvi
 * @Created 31.08.2023
 */

public class Constants {

    //SECURITY
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";

    public static final String TOKEN_KEY = "token";

    public static final String EMAIL_KEY = "email";

    public static final String AUTHORITIES = "authorities";

    public static final String MARKET_ISSUER = "MARKET_ISSUER";

    public static final String CUSTOMER_MANAGEMENT_SERVICE = "CUSTOMER_MANAGEMENT_SERVICE";

    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 432_000_000;

    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;

    public static final String[] PUBLIC_URLS = {"/user/refresh/token/**", "/user/verify/account/**", "/user/verify/password/**", "/user/register/**", "/user/login/**", "/user/verify/code/**", "/user/resetpassword/**", "/user/image/**", "/user/new/password"};

    public static final String[] PUBLIC_ROUTES = {"/user/new/password","/user/login", "/user/verify/code", "/user/register", "/user/refresh/token","/user/image"};

    //REQUEST
    public static final String USER_AGENT_HEADER = "user-agent";

    public static final String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";




    //DATE
    public static String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

}
