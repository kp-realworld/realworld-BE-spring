package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByUserIdAndId(Long userId, Long id);
}
