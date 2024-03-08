package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.article.ArticleResponse;
import com.hotkimho.realworldapi.dto.article.UpdateArticleRequest;
import com.hotkimho.realworldapi.dto.articlelike.ArticleLikeInfo;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.ArticleLikeService;
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
    private final ArticleLikeService articleLikeService;
    @Autowired()
    public ArticleController(ArticleService articleService, ArticleLikeService articleLikeService) {
        this.articleService = articleService;
        this.articleLikeService = articleLikeService;
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
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElse(0L);

        ArticleDto articleDto = articleService.findByUserIdAndId(author_id, article_id);

        ArticleLikeInfo likeInfo = articleLikeService.getArticleLikeInfo(currentUserId, article_id);
        articleDto.setFavorited(likeInfo.isFavorited());
        articleDto.setFavoriteCount(likeInfo.getLikeCount());

        return ResponseEntity.ok()
                .body(articleDto);
    }

    @PutMapping("/article/{article_id}")
    public ResponseEntity<ArticleDto> updateArticle(
            @PathVariable("article_id") Long article_id,
            @RequestBody UpdateArticleRequest request
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        ArticleDto updatedArticle = articleService.update(currentUserId, article_id, request);

        ArticleLikeInfo likeInfo = articleLikeService.getArticleLikeInfo(currentUserId, article_id);
        updatedArticle.setFavorited(likeInfo.isFavorited());
        updatedArticle.setFavoriteCount(likeInfo.getLikeCount());

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
