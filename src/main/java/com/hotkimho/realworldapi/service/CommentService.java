package com.hotkimho.realworldapi.service;

import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.Comment;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.comment.AddCommentRequest;
import com.hotkimho.realworldapi.dto.comment.CommentDto;
import com.hotkimho.realworldapi.dto.comment.CommentListDto;
import com.hotkimho.realworldapi.dto.comment.CommentResponse;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.ArticleRepository;
import com.hotkimho.realworldapi.repository.CommentRepository;
import com.hotkimho.realworldapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public CommentResponse save(AddCommentRequest request, Long articleId, Long currentUserId) {

        User author = userRepository.findById(currentUserId)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found user id: " + currentUserId));

        Article article = new Article(articleId);
        try {
            return new CommentResponse(commentRepository.save(request.toEntity(article, author)));
        } catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.BAD_REQUEST, "comment save error: " + e.getMessage());
        }
    }

    public CommentListDto getComments(Long articleId) {

        System.out.println("qwekopqwejio "+ articleId);
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        System.out.println("------------------------- " + comments.size());


        return new CommentListDto(comments);
    }

    // update comment
    @Transactional
    public CommentResponse updateComment(
            Long commentId,
            Long currentUserId,
            String body,
            Long articleId,
            Long authorId) {

        // article 소유 확인
        if (!articleRepository.existsByIdAndUserUserId(articleId,authorId)) {
            throw new DefaultErrorException(HttpStatus.FORBIDDEN, "article not found or not authorized");
        }

        // comment 소유 확인
        if (!commentRepository.existsByIdAndUserUserId(commentId,currentUserId)) {
            throw new DefaultErrorException(HttpStatus.FORBIDDEN, "comment not found or not authorized");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found user id: " + currentUserId));

        // 소유 확인을 하지않고 위랑 아래를 1개로 합칠까?
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found comment id: " + commentId));

        comment.update(body);
        comment.setUser(user);

        return new CommentResponse(comment);
    }

    // delete comment
    @Transactional
    public void deleteComment(
            Long commentId,
            Long currentUserId,
            Long articleId,
        Long authorId) {

        // article 소유 확인
        if (!articleRepository.existsByIdAndUserUserId(articleId,authorId)) {
            throw new DefaultErrorException(HttpStatus.FORBIDDEN, "article not found or not authorized");
        }
        // comment 소유 확인
        if (!commentRepository.existsByIdAndUserUserId(commentId,currentUserId)) {
            throw new DefaultErrorException(HttpStatus.FORBIDDEN, "comment not found or not authorized");
        }

        try {
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "comment delete error: " + e.getMessage());
        }
    }
}
