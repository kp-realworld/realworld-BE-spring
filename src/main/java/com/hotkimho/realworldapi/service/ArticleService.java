package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.UpdateArticleRequest;
import com.hotkimho.realworldapi.repository.ArticleRepository;
import jakarta.transaction.Transactional;
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

        Article article = articleRepository.findByUserIdAndId(author_id,article_id)
                .orElseThrow(() -> new IllegalArgumentException("not found article id: " + article_id));
        // print before update


        article.update(request.getTitle(), request.getDescription(), request.getBody());

     //   articleRepository.flush();
        // todo update tag
        // print after update
        return article;
    }

    public void delete(Long author_id, Long article_id) {
        System.out.println("author_id" + author_id);
        System.out.println("article_id" + article_id);
        articleRepository.deleteByUserIdAndId(author_id, article_id);
    }
}
