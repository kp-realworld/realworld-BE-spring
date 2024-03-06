package com.hotkimho.realworldapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.user.VerifyEmail;
import com.hotkimho.realworldapi.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @DisplayName("verifyEmail: 이메일 중복 확인(중복 없음)")
    @Test
    void verifyEmail() throws Exception {
        // given
        final String url = "/user/verify-email";
        final VerifyEmail request = new VerifyEmail("test0406@test.com");

        final String requestBody = objectMapper.writeValueAsString(request);


        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        Assertions.assertThat(request.getEmail()).isEqualTo("test0406@test.com");
    }

    @DisplayName("verifyUsername: 유저네임 중복 확인(중복 없음)")
    @Test
    void verifyUsername() throws Exception {

        // given
        final String url = "/user/verify-username";
        final VerifyEmail request = new VerifyEmail("test0406");

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        Assertions.assertThat(request.getEmail()).isEqualTo("test0406");

    }
}