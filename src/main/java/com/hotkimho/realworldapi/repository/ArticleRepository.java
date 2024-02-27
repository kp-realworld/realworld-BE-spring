package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
