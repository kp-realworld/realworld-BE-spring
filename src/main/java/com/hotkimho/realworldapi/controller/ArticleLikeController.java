package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.articlelike.ArticleLikeInfo;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.ArticleLikeService;
import com.hotkimho.realworldapi.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @Autowired
    public ArticleLikeController(ArticleLikeService articleLikeService) {
        this.articleLikeService = articleLikeService;
    }

    @PostMapping("/user/{author_id}/article/{article_id}/like")
    public ResponseEntity<ArticleDto> likeArticle(
            @PathVariable("author_id") Long author_id,
            @PathVariable("article_id") Long article_id
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        ArticleDto articleDto = articleLikeService.addArticleLike(author_id, article_id, currentUserId);

        ArticleLikeInfo likeInfo = articleLikeService.getArticleLikeInfo(currentUserId, article_id);
        articleDto.setFavorited(likeInfo.isFavorited());
        articleDto.setFavoriteCount(likeInfo.getLikeCount());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleDto);
    }

    @DeleteMapping("/user/{author_id}/article/{article_id}/like")
    public ResponseEntity<Void> deleteArticleLike(
            @PathVariable("author_id") Long author_id,
            @PathVariable("article_id") Long article_id
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        articleLikeService.deleteArticleLike(author_id, article_id, currentUserId);

        return ResponseEntity.noContent().build();
    }
}
