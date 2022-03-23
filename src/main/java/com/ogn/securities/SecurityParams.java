package com.ogn.securities;

public interface SecurityParams {
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final String SECRET = "naite@gmail.com";
    public static final long EXPIRATION = 7*1000*60*60*24; // (No.of days * Milliseconds *Seconds * Minutes * Hours)); Exp:31 days (31*1000*60*60*24)
    public static final String HEADER_PREFIX = "Bearer ";

}
