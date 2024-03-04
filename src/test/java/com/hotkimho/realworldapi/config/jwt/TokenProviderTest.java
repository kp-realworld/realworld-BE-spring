package com.hotkimho.realworldapi.config.jwt;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken: 토큰 생성")
    @Test
    void generateToken() {
        // given
        User testUser = userRepository.save(
                User.builder()
                        .username("kimhoho1")
                        .email("hohoho@naver.com")
                        .password("test")
                        .build()
        );

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        Long userID = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);

         Assertions.assertThat(userID).isEqualTo(testUser.getUserId());
    }

    @DisplayName("validateToken: 토큰 유효성 검사 실패")
    @Test
    void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .build()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validateToken(token);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @DisplayName("validateToken: 토큰 유효성 검사 성공")
    @Test
    void validToken_validToken() {
        // given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validateToken(token);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반 인증 정보 조회")
    @Test
    void getAuthentication() {
        // given
        String email = "hohoho@naver.com";
        String token = JwtFactory.builder()
                .subject(email)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        Assertions.assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(email);
    }

    @DisplayName("getUserId(): 토큰 기반 사용자 식별 정보 조회")
    @Test
    void getUserId() {
        // given
        Long userId = 50L;
        String token = JwtFactory.builder()
                .claims(Map.of("userId", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long result = tokenProvider.getUserId(token);

        // then
        Assertions.assertThat(result).isEqualTo(userId);
    }
}

