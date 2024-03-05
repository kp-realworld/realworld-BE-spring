package com.hotkimho.realworldapi.util;

public final class AuthUtil {
    private static final String BEARER_PREFIX = "Bearer ";

    private AuthUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");

    }
    public static String getAccessToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }


        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        return token;
    }

}
