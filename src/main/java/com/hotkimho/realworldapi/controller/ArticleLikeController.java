package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.article.ArticleResponse;
import com.hotkimho.realworldapi.dto.articlelike.ArticleLikeInfo;
import com.hotkimho.realworldapi.dto.common.ErrorResponse;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.ArticleLikeService;
import com.hotkimho.realworldapi.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "게시글 좋아요", description = "게시글을 좋아요 합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "게시글 좋아요 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<ArticleResponse> likeArticle(
            @PathVariable("author_id") Long author_id,
            @PathVariable("article_id") Long article_id
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        ArticleResponse articleDto = articleLikeService.addArticleLike(author_id, article_id, currentUserId);

        ArticleLikeInfo likeInfo = articleLikeService.getArticleLikeInfo(currentUserId, article_id);
        articleDto.getArticle().setFavorited(likeInfo.isFavorited());
        articleDto.getArticle().setFavoriteCount(likeInfo.getLikeCount());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleDto);
    }

    @DeleteMapping("/user/{author_id}/article/{article_id}/like")
    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요를 취소합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "게시글 좋아요 취소 성공", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
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
