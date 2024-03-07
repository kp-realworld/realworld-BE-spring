package com.hotkimho.realworldapi.util;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.service.CustomUserDetailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class AuthUtil {
    private static final String BEARER_PREFIX = "Bearer ";

    private AuthUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");

    }
    public static String getAccessToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    public static Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();

            return Optional.of(currentUser.getUserId());
        }
        return Optional.empty();
    }
}
