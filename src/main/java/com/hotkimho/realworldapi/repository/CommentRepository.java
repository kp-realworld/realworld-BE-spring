package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(Long articleId);

    @Transactional
    @Modifying
    @Query("update Comment c set c.deletedAt = CURRENT_TIMESTAMP where c.id = :id and c.deletedAt is null")
    void deleteById(@Param("id") Long id);

    // userid and comment id로 확인
    boolean existsByIdAndUserUserId(Long commentId, Long userId);
}
