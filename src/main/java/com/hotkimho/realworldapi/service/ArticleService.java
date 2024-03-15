package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.*;
import com.hotkimho.realworldapi.dto.article.*;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.*;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArticleService {


    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Autowired
    public ArticleService(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            ArticleTagRepository articleTagRepository,
            ArticleLikeRepository articleLikeRepository,
            ArticleLikeCountRepository articleLikeCountRepository
            ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleTagRepository = articleTagRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.articleLikeCountRepository = articleLikeCountRepository;
    }

    @Transactional
    public ArticleResponse save(AddArticleRequest request) {

        // read user
        User author = userRepository.findById(request.getAuthor().getUserId())
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found user id: " + request.getAuthor().getUserId()));

        // set author
        request.setAuthor(author);

        try {
            Article savedArticle = articleRepository.save(request.toEntity());
            List<ArticleTag> articleTags = request.toArticleTagsEntity(savedArticle);
            List<ArticleTag> savedArticleTags =  articleTagRepository.saveAll(articleTags);
            savedArticle.setArticleTags(savedArticleTags);
            savedArticle.setUser(author);
            return new ArticleResponse(savedArticle);
        } catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "article save error: " + e.getMessage());
        }
    }

    @Transactional
    public ArticleResponse findByUserIdAndId(Long authorId, Long articleId) {
        // article 소유 확인
        if (!articleRepository.existsByIdAndUserUserId(articleId, authorId)) {
            throw new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + articleId);
        }

        // read article
        Article article =  articleRepository.findByIdWithUser(articleId)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + articleId));

        // set data
        return new ArticleResponse(article);
    }

    @Transactional
    public ArticleResponse update(
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


        // set data
        article.setArticleTags(savedArticleTags);
        article.setUser(user);

        return new ArticleResponse(article);
    }

    public void delete(Long author_id, Long article_id) {
        // article 소유 확인
        if (!articleRepository.existsByIdAndUserUserId(article_id, author_id)) {
            throw new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + article_id);
        }

        articleRepository.deleteByUserUserIdAndId(author_id, article_id);
    }

    public List<Article> getArticles(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<Article> articlePage = articleRepository.findAll(pageable);

        return articlePage.stream()
                .toList();
    }

    public List<ArticleTag> getArticlesByTag(int limit, int offset, String tag) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<ArticleTag> articlePage = articleTagRepository.findByTagWithPagination(tag, pageable);

        return articlePage.stream()
                .toList();
    }

    public List<Article> getArticlesByIds(List<Long> articleIds) {
        return articleRepository.findAllById(articleIds);
    }

    public List<Article> getArticlesByUserId(int limit, int offset, Long userId) {
        return articleRepository.findByUserIdWithPagination(userId, PageRequest.of(offset, limit, Sort.by("id").descending()))
                .stream()
                .toList();
    }

}
