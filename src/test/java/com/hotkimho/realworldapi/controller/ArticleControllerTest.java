package com.hotkimho.realworldapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.User;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
//        articleRepository.deleteAll();
    }

    @DisplayName("addArticle: 게시글 등록")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/article";
        final AddArticleRequest request = new AddArticleRequest(
                "제목",
                "설명",
                "내용",
                new String[]{"태그1", "태그2"},
                new User(72L));


        final String requestBody = objectMapper.writeValueAsString(request);


        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andDo(print());

        // then
        result.andExpect(status().isCreated());

        Article article = articleRepository.findAll().get(0);


        // assertthat
        Assertions.assertThat(article.getTitle()).isEqualTo("제목");
        Assertions.assertThat(article.getDescription()).isEqualTo("설명");
        Assertions.assertThat(article.getBody()).isEqualTo("내용");
    }

    @DisplayName("getArticle: 게시글 조회")
    @Test
    public void getArticle() throws Exception {
        // given
        final String url = "/user/{author_id}/article/{article_id}";
        final AddArticleRequest request = new AddArticleRequest("제목", "설명", "내용", new String[]{"태그1", "태그2"}, new User(50L));

        Article savedArticle = articleRepository.save(Article.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .body(request.getBody())
                .build());

        System.out.println("saved id : "+ savedArticle.getId());
        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getUser().getUserId(), savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.body").value(request.getBody()));
    }

    @DisplayName("updateArticle: 게시글 수정")
    @Test
    public void updateArticle() throws Exception {
        // given
        final String url = "/user/{author_id}/article/{article_id}";
        final AddArticleRequest request = new AddArticleRequest("제목", "설명", "내용", new String[]{"태그1", "태그2"}, new User(50L));

        Article savedArticle = articleRepository.save(Article.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .body(request.getBody())
                .build());

        AddArticleRequest updateRequest = new AddArticleRequest("수정된 제목22", "수정된 설명", "수정된 내용", new String[]{"수정된 태그1", "수정된 태그2"}, new User(50L));

        final String requestBody = objectMapper.writeValueAsString(updateRequest);

        // when
        final ResultActions resultActions = mockMvc.perform(put(url, savedArticle.getUser().getUserId(), savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateRequest.getTitle()))
                .andExpect(jsonPath("$.description").value(updateRequest.getDescription()))
                .andExpect(jsonPath("$.body").value(updateRequest.getBody()));
    }

    @DisplayName("deleteArticle: 게시글 삭제")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/user/{author_id}/article/{article_id}";
        final AddArticleRequest request = new AddArticleRequest("제목", "설명", "내용", new String[]{"태그1", "태그2"}, new User(50L));

        Article savedArticle = articleRepository.save(Article.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .body(request.getBody())
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(delete(url, savedArticle.getUser().getUserId(), savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isNoContent());
    }
}