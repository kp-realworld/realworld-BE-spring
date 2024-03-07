package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.article.ArticleResponse;
import com.hotkimho.realworldapi.dto.article.UpdateArticleRequest;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.ArticleService;

import com.hotkimho.realworldapi.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired()
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/article")
    public ResponseEntity<ArticleDto> addArticle(@RequestBody AddArticleRequest request) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));
        System.out.println("tags " + request.getTagList());

        request.setAuthor(new User(currentUserId));

        ArticleDto savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/user/{author_id}/article/{article_id}")
    public ResponseEntity<ArticleDto> getArticle(
            @PathVariable("author_id") Long author_id,
            @PathVariable("article_id") Long article_id
    ) {
        ArticleDto articledto = articleService.findByUserIdAndId(author_id, article_id);
        return ResponseEntity.ok()
                .body(articledto);
    }

    @PutMapping("/article/{article_id}")
    public ResponseEntity<ArticleDto> updateArticle(
            @PathVariable("article_id") Long article_id,
            @RequestBody UpdateArticleRequest request
    ) {
        Long author_id = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        ArticleDto updatedArticle = articleService.update(author_id, article_id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }

    @DeleteMapping("/article/{article_id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("article_id") Long article_id
    ) {
        Long author_id = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));


        articleService.delete(author_id, article_id);
        return ResponseEntity.noContent().build();
    }
}
