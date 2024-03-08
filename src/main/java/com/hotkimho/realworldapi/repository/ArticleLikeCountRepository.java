package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.ArticleLike;
import com.hotkimho.realworldapi.domain.ArticleLikeCount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE ArticleLikeCount alc SET alc.count = alc.count - 1 WHERE alc.articleId = :articleId AND alc.count > 0")
    int decrementCountByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Transactional
    @Query("UPDATE ArticleLikeCount alc SET alc.count = alc.count + 1 WHERE alc.articleId = :articleId")
    int incrementCountByArticleId(@Param("articleId") Long articleId);


    // read  ㅁ

}
