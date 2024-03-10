package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Article;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByUserUserIdAndId(Long userId, Long id);
    // delete by user id and article id
    @Transactional
    @Modifying
    @Query("update Article a set a.deletedAt = CURRENT_TIMESTAMP where a.user.userId = :userId and a.id = :id and a.deletedAt is null")
    void deleteByUserUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);

    @Query("select a from Article a join fetch a.user where a.id = :articleId")
    Optional<Article> findByIdWithUser(@Param("articleId") Long articleId);

    boolean existsByIdAndUserUserId(Long articleId, Long userId);

    Page<Article> findAll(Pageable pageable);

    @Query("select a from Article a join fetch a.user where a.user.userId = :userId")
    Page<Article> findByUserIdWithPagination(@Param("userId") Long userId, Pageable pageable);
}
