package com.hotkimho.realworldapi.config.jwt;

import com.hotkimho.realworldapi.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    private final  TokenAuthenticationFilter tokenAuthenticationFilter; // JWT 필터 추가

    private final TokenProvider tokenProvider;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String[] ExceptionAPIS = {
            "/user/signup",
            "/user/signin",
            "/token-refresh"
    };

    private final String[] ExceptionGetAPIS = {
            "/user/{author_id}/article/{article_id}",
            "/articles",
            "/articles/tag",
            "/user/{author_id}/article/{article_id}/comments",
            "/user/{user_id}/profile",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .httpBasic().disable() // HTTP 기본 인증 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리 정책: 상태 없음
                .and()
                .authorizeRequests()
                .requestMatchers("/**").permitAll() // 인증 없이 접근 허용 경로
                .requestMatchers("/user/signup", "/user/signin","/token-refresh").permitAll() // 인증 없이 접근 허용 경로
//                .requestMatchers("/**").permitAll() // 인증 없이 접근 허용 경로
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll() // 인증 없이 접근 허용 경로
                .requestMatchers(HttpMethod.GET, ExceptionGetAPIS).permitAll() // 인증 없이 접근 허용 경로
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailService)
//                .passwordEncoder(bCryptPasswordEncoder());
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManagerBean(AuthenticationManagerBuilder auth) throws Exception {
//        if (customUserDetailService == null) {
//            throw new Exception("customUserDetailService is null");
//        }
//
//        BCryptPasswordEncoder bc = bCryptPasswordEncoder();
//        if (bc == null) {
//            throw new Exception("bCryptPasswordEncoder is null");
//        }
//
//
//        auth.userDetailsService(customUserDetailService)
//                .passwordEncoder(bc);
//        return auth.build();
//    }
}