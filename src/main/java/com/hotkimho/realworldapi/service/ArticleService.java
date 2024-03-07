package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleTag;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.article.UpdateArticleRequest;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.ArticleRepository;
import com.hotkimho.realworldapi.repository.ArticleTagRepository;
import com.hotkimho.realworldapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;


@Service
public class ArticleService {


    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleTagRepository articleTagRepository;
    @Autowired
    public ArticleService(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            ArticleTagRepository articleTagRepository
            ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleTagRepository = articleTagRepository;
    }

    @Transactional
    public ArticleDto save(AddArticleRequest request) {

        // read user
        User author = userRepository.findById(request.getAuthor().getUserId())
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found user id: " + request.getAuthor().getUserId()));

        // set author
        request.setAuthor(author);

        // save article
        Article savedArticle = articleRepository.save(request.toEntity());

        // save article tag
        List<ArticleTag> articleTags = request.toArticleTagsEntity(savedArticle);
        List<ArticleTag> savedArticleTags =  articleTagRepository.saveAll(articleTags);
        savedArticle.setArticleTags(savedArticleTags);

        return new ArticleDto(savedArticle);
    }

    public ArticleDto findByUserIdAndId(Long authorId, Long id) {
        Article article =  articleRepository.findByIdWithUser(id)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + id));

        return new ArticleDto(article);
    }

    @Transactional
    public ArticleDto update(
            Long author_id,
            Long article_id,
            UpdateArticleRequest request)
    {

        // article 소유 확인
        if (!articleRepository.existsByIdAndUserUserId(article_id, author_id)) {
            throw new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + article_id);
        }

        // read user
        User user = userRepository.findById(author_id)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found user id: " + author_id));


        // read article
        Article article = articleRepository.findByUserUserIdAndId(author_id,article_id)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + article_id));

        // update article
        article.update(request.getTitle(), request.getDescription(), request.getBody());

        // delete article tag
        articleTagRepository.deleteByArticleId(article_id);

        // add article tag
        List<ArticleTag> articleTags = request.toArticleTagsEntity(article);
        List<ArticleTag> savedArticleTags =  articleTagRepository.saveAll(articleTags);

        // set user and tag
        article.setArticleTags(savedArticleTags);
        article.setUser(user);

        return new ArticleDto(article);
    }

    public void delete(Long author_id, Long article_id) {
        // article 소유 확인
        if (!articleRepository.existsByIdAndUserUserId(article_id, author_id)) {
            throw new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + article_id);
        }

        articleRepository.deleteByUserUserIdAndId(author_id, article_id);
    }
}
