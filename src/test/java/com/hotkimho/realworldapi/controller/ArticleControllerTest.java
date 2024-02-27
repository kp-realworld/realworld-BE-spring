package com.hotkimho.realworldapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    public void setMockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        articleRepository.deleteAll();
    }

    @DisplayName("addArticle: 게시글 등록")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/article";
        final AddArticleRequest request = new AddArticleRequest("제목", "설명", "내용", new String[]{"태그1", "태그2"});


        final String requestBody = objectMapper.writeValueAsString(request);


        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        Article article = articleRepository.findAll().get(0);


        // assertthat
        Assertions.assertThat(article.getTitle()).isEqualTo("제목");
        Assertions.assertThat(article.getDescription()).isEqualTo("설명");
        Assertions.assertThat(article.getBody()).isEqualTo("내용");
    }
}