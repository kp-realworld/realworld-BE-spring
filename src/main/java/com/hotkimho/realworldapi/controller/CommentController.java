package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.comment.AddCommentRequest;
import com.hotkimho.realworldapi.dto.comment.CommentDto;
import com.hotkimho.realworldapi.dto.comment.CommentListDto;
import com.hotkimho.realworldapi.dto.comment.CommentResponse;
import com.hotkimho.realworldapi.dto.common.ErrorResponse;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.CommentService;
import com.hotkimho.realworldapi.util.AuthUtil;
import org.hibernate.binder.internal.CommentsBinder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    /*
     @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = ArticleListDto.class))),
    })
     */
    @PostMapping("/user/{author_id}/article/{article_id}/comment")
    @Operation(summary = "댓글 추가", description = "댓글을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 추가 성공", content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "댓글 추가 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId,
            @RequestBody AddCommentRequest request
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));


        CommentResponse savedComment = commentService.save(request, articleId, currentUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedComment);
    }

    @GetMapping("/user/{author_id}/article/{article_id}/comments")
    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommentListDto.class))),
            @ApiResponse(responseCode = "400", description = "댓글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<CommentListDto> getComments(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId
    ) {
        CommentListDto commentListDto = commentService.getComments(articleId);

        return ResponseEntity.ok(commentListDto);
    }

    @PutMapping("/user/{author_id}/article/{article_id}/comment/{comment_id}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId,
            @PathVariable("comment_id") Long commentId,
            @RequestBody AddCommentRequest request
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        CommentResponse updatedComment = commentService.updateComment(
                commentId,
                currentUserId,
                request.getBody(),
                articleId,
                authorId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/user/{author_id}/article/{article_id}/comment/{comment_id}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Void> deleteComment(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId,
            @PathVariable("comment_id") Long commentId
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        commentService.deleteComment(
                commentId,
                currentUserId,
                articleId,
                authorId);

        return ResponseEntity.noContent().build();
    }
}
