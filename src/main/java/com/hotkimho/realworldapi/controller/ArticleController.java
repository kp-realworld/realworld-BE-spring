package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.*;
import com.hotkimho.realworldapi.dto.article.*;
import com.hotkimho.realworldapi.dto.articlelike.ArticleLikeInfo;
import com.hotkimho.realworldapi.dto.common.ErrorResponse;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.ArticleLikeService;
import com.hotkimho.realworldapi.service.ArticleService;

import com.hotkimho.realworldapi.service.FollowService;
import com.hotkimho.realworldapi.util.AuthUtil;
import com.hotkimho.realworldapi.util.Convert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleLikeService articleLikeService;
    private final FollowService followService;
    @Autowired()
    public ArticleController(
            ArticleService articleService,
            ArticleLikeService articleLikeService,
            FollowService followService) {
        this.articleService = articleService;
        this.articleLikeService = articleLikeService;
        this.followService = followService;
    }

    @PostMapping("/article")
    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공", content = @Content( schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "게시글 작성 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));
        System.out.println("tags " + request.getTagList());

        request.setAuthor(new User(currentUserId));
        ArticleResponse savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/user/{author_id}/article/{article_id}")
    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElse(0L);

        ArticleResponse articleDto = articleService.findByUserIdAndId(authorId, articleId);

        ArticleLikeInfo likeInfo = articleLikeService.getArticleLikeInfo(currentUserId, articleId);
        articleDto.getArticle().setFavorited(likeInfo.isFavorited());
        articleDto.getArticle().setFavoriteCount(likeInfo.getLikeCount());

        articleDto.getArticle().getAuthor().setFollowing(followService.isFollowing(currentUserId, authorId));
        return ResponseEntity.ok()
                .body(articleDto);
    }

    @PutMapping("/article/{article_id}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글 또는 작성자가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable("article_id") Long articleId,
            @RequestBody UpdateArticleRequest request
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        ArticleResponse updatedArticle = articleService.update(currentUserId, articleId, request);

        ArticleLikeInfo likeInfo = articleLikeService.getArticleLikeInfo(currentUserId, articleId);
        updatedArticle.getArticle().setFavorited(likeInfo.isFavorited());
        updatedArticle.getArticle().setFavoriteCount(likeInfo.getLikeCount());

        return ResponseEntity.ok()
                .body(updatedArticle);
    }

    @DeleteMapping("/article/{article_id}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글 또는 작성자가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("article_id") Long article_id
    ) {
        Long author_id = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));


        articleService.delete(author_id, article_id);
        return ResponseEntity.noContent().build();
    }

    // article 페이지네이션
    @GetMapping("/articles")
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = ArticleListDto.class))),
    })
    public ResponseEntity<ArticleListDto> getArticles(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElse(0L);

        List<Article> articleList = articleService.getArticles(limit, offset);
        List<Long> articleIds = articleList.stream()
                .map(Article::getId)
                .toList();
        List<ArticleLikeCount> articleLikeCounts = articleLikeService.findByArticleIdIn(articleIds);

        List<ArticleLike> articleLikes = new ArrayList<>();
        if (currentUserId != 0) {
            articleLikes = articleLikeService.findByUserIdAndArticleIdIn(currentUserId, articleIds);
        }

        List<Long> authorIds = articleList.stream()
                .map(article -> article.getUser().getUserId())
                .toList();

        List<Follow> follows = followService.getFollowers(currentUserId, authorIds);

        ArticleListDto articleListDto = Convert.convertToArticleListDtoWithLikeInfo(articleList, articleLikes, articleLikeCounts,follows);
        return ResponseEntity.ok()
                .body(articleListDto);
    }

    // article tag 페이지네이션
    @GetMapping("/articles/tag")
    @Operation(summary = "태그별 게시글 목록 조회", description = "태그별 게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그별 게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = ArticleListDto.class))),
    })
    public ResponseEntity<ArticleListDto> getArticlesByTag(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "tag") String tag
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElse(0L);

        List<ArticleTag> articleTags = articleService.getArticlesByTag(limit, offset, tag);
        List<Long> articleIds = articleTags.stream()
                .map(articleTag -> articleTag.getArticle().getId())
                .toList();

        List<Article> articleList = articleService.getArticlesByIds(articleIds);
        List<ArticleLikeCount> articleLikeCounts = articleLikeService.findByArticleIdIn(articleIds);

        List<ArticleLike> articleLikes = new ArrayList<>();
        if (currentUserId != 0) {
            articleLikes = articleLikeService.findByUserIdAndArticleIdIn(currentUserId, articleIds);
        }


        List<Long> authorIds = articleList.stream()
                .map(article -> article.getUser().getUserId())
                .toList();
        List<Follow> follows = followService.getFollowers(currentUserId, authorIds);


        ArticleListDto articleListDto = Convert.convertToArticleListDtoWithLikeInfo(articleList, articleLikes, articleLikeCounts, follows);
        // id 역순
        articleListDto.getArticles().sort((a, b) -> b.getArticleId().compareTo(a.getArticleId()));
        return ResponseEntity.ok()
                .body(articleListDto);
    }

    @GetMapping("/my/articles")
    @Operation(summary = "내 게시글 목록 조회", description = "내 게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = ArticleListDto.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<ArticleListDto> getMyArticles(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        List<Article> articleList = articleService.getArticlesByUserId(limit, offset, currentUserId);
        List<Long> articleIds = articleList.stream()
                .map(Article::getId)
                .toList();

        List<ArticleLikeCount> articleLikeCounts = articleLikeService.findByArticleIdIn(articleIds);

        List<ArticleLike> articleLikes = articleLikeService.findByUserIdAndArticleIdIn(currentUserId, articleIds);

        List<Long> authorIds = articleList.stream()
                .map(article -> article.getUser().getUserId())
                .toList();
        List<Follow> follows = followService.getFollowers(currentUserId, authorIds);


        ArticleListDto articleListDto = Convert.convertToArticleListDtoWithLikeInfo(articleList, articleLikes, articleLikeCounts,follows);
        return ResponseEntity.ok()
                .body(articleListDto);
    }

    @GetMapping("/user/{author_id}/articles")
    @Operation(summary = "유저 게시글 목록 조회", description = "유저 게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = ArticleListDto.class))),
    })
    public ResponseEntity<ArticleListDto> getArticlesByUserId(
            @PathVariable("author_id") Long author_id,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElse(0L);

        List<Article> articleList = articleService.getArticlesByUserId(limit, offset, author_id);
        List<Long> articleIds = articleList.stream()
                .map(Article::getId)
                .toList();

        List<ArticleLikeCount> articleLikeCounts = articleLikeService.findByArticleIdIn(articleIds);

        List<ArticleLike> articleLikes = new ArrayList<>();
        if (currentUserId != 0) {
            articleLikes = articleLikeService.findByUserIdAndArticleIdIn(currentUserId, articleIds);
        }

        List<Long> authorIds = articleList.stream()
                .map(article -> article.getUser().getUserId())
                .toList();
        List<Follow> follows = followService.getFollowers(currentUserId, authorIds);

        ArticleListDto articleListDto = Convert.convertToArticleListDtoWithLikeInfo(articleList, articleLikes, articleLikeCounts, follows);
        return ResponseEntity.ok()
                .body(articleListDto);
    }
}
