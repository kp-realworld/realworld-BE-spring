package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.UpdateArticleRequest;
import com.hotkimho.realworldapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public Article findByUserIdAndId(Long authorId, Long id) {
        return articleRepository.findByUserIdAndId(authorId, id)
                .orElseThrow(() -> new IllegalArgumentException("not found article id: " + id));
    }


    @Transactional
    public Article update(
            Long author_id,
            Long article_id,
            UpdateArticleRequest request)
    {
        Article article = articleRepository.findById(article_id)
                .orElseThrow(() -> new IllegalArgumentException("not found article id: " + article_id));
        // print before update
        System.out.println("before title" + article.getTitle());
        System.out.println("before description" + article.getDescription());
        System.out.println("before body" + article.getBody());

        article.update(request.getTitle(), request.getDescription(), request.getBody());
        // todo update tag
        // print after update
        System.out.println("after title" + article.getTitle());
        System.out.println("after description" + article.getDescription());
        System.out.println("after body" + article.getBody());
        return article;
    }
}
