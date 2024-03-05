package com.hotkimho.realworldapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotkimho.realworldapi.config.jwt.JwtFactory;
import com.hotkimho.realworldapi.config.jwt.JwtProperties;
import com.hotkimho.realworldapi.domain.RefreshToken;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.CreateAccessTokenRequest;
import com.hotkimho.realworldapi.repository.RefreshTokenRepository;
import com.hotkimho.realworldapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @DisplayName("createAccessToken: 액세스 토큰 생성")
    @Test
    public void createAccessToken() throws Exception {
        // given
        final String url = "/token-refresh";

        User testUser = userRepository.save(
                User.builder()
                        .username("spho1")
                        .email("spho1@test.com")
                        .password("test")
                        .build());

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("userId", testUser.getUserId()))
                .build().createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testUser.getUserId(), refreshToken));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);

//        final String requestBody = objectMapper.writeValueAsString(request);
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization","Bearer "+ refreshToken));
        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty());
    }
}