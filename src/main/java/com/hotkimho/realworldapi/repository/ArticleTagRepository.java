package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.ArticleTag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    List<ArticleTag> findByArticleId(Long articleId);

    @Transactional
    @Modifying
    @Query("update ArticleTag at set at.deletedAt = CURRENT_TIMESTAMP where at.article.id = :articleId and at.deletedAt is null")
    void deleteByArticleId(@Param("articleId") Long articleId);
}
