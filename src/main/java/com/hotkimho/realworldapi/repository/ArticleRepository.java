package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Article;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByUserIdAndId(Long userId, Long id);
    // delete by user id and article id
    @Transactional
    @Modifying
    @Query("update Article a set a.deletedAt = CURRENT_TIMESTAMP where a.userId = :userId and a.id = :id and a.deletedAt is null")
    void deleteByUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);
}
