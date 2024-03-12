package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.comment.AddCommentRequest;
import com.hotkimho.realworldapi.dto.comment.CommentDto;
import com.hotkimho.realworldapi.dto.comment.CommentListDto;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.CommentService;
import com.hotkimho.realworldapi.util.AuthUtil;
import org.hibernate.binder.internal.CommentsBinder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/user/{author_id}/article/{article_id}/comment")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId,
            @RequestBody AddCommentRequest request
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));


        CommentDto savedComment = commentService.save(request, articleId, currentUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedComment);
    }

    @GetMapping("/user/{author_id}/article/{article_id}/comments")
    public ResponseEntity<CommentListDto> getComments(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId
    ) {
        CommentListDto commentListDto = commentService.getComments(articleId);

        return ResponseEntity.ok(commentListDto);
    }

    @PutMapping("/user/{author_id}/article/{article_id}/comment/{comment_id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("author_id") Long authorId,
            @PathVariable("article_id") Long articleId,
            @PathVariable("comment_id") Long commentId,
            @RequestBody AddCommentRequest request
    ) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        CommentDto updatedComment = commentService.updateComment(
                commentId,
                currentUserId,
                request.getBody(),
                articleId,
                authorId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/user/{author_id}/article/{article_id}/comment/{comment_id}")
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
