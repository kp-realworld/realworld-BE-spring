package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    // read by user id and article id
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    void deleteByUserIdAndArticleId(Long userId, Long articleId);

    List<ArticleLike> findByUserIdAndArticleIdIn(Long userId, List<Long> articleIds);
}
