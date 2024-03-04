package com.hotkimho.realworldapi.service;

import com.hotkimho.realworldapi.config.jwt.JwtProperties;
import com.hotkimho.realworldapi.config.jwt.TokenProvider;
import com.hotkimho.realworldapi.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtProperties jwtProperties;
    public TokenService(TokenProvider tokenProvider, RefreshTokenService refreshTokenService, UserService userService, JwtProperties jwtProperties) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    public String createAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        Long userId = refreshTokenService.findByFreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(jwtProperties.getAccessExp()));
    }
}
