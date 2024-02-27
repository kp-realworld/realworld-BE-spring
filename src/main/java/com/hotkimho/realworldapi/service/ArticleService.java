package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ArticleService {


    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(AddArticleRequest request) {
        return articleRepository.save(request.toEntity());
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found article id: " + id));
    }
}
