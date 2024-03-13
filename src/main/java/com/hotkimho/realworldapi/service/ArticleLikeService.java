package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleLike;
import com.hotkimho.realworldapi.domain.ArticleLikeCount;
import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.article.ArticleResponse;
import com.hotkimho.realworldapi.dto.articlelike.ArticleLikeInfo;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleLikeService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final RedisTemplate<String, Integer> redisTemplate;


    @Autowired
    public ArticleLikeService(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            ArticleTagRepository articleTagRepository,
            ArticleLikeRepository articleLikeRepository,
            ArticleLikeCountRepository articleLikeCountRepository,
            RedisTemplate<String, Integer> redisTemplate
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleTagRepository = articleTagRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.articleLikeCountRepository = articleLikeCountRepository;
        this.redisTemplate = redisTemplate;
    }

    // userid, articleid를 받아서 좋아요, 좋아요 수 조회
    public ArticleLikeInfo getArticleLikeInfo(Long userId, Long articleId) {

        // read article like
        boolean isFavorited = articleLikeRepository.existsByUserIdAndArticleId(userId, articleId);

        // read article like count
        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
                .orElse(new ArticleLikeCount(articleId, 0));

        return new ArticleLikeInfo(articleLikeCount.getCount(), isFavorited);
    }


    @Transactional
    public ArticleResponse addArticleLike(Long authorId, Long articleId, Long currentUserId) {
        // article 확인
        if (!articleRepository.existsByIdAndUserUserId(articleId, authorId)) {
            throw new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + articleId);
        }

        // 좋아요 하지 않은 경우, 좋아요 추가
        if (!articleLikeRepository.existsByUserIdAndArticleId(currentUserId, articleId)) {
            articleLikeRepository.save(new ArticleLike(currentUserId, articleId));
        }

        String articleLikeCountKey = "article:" + articleId;
        try {
            ArticleLikeCount articleLikeCount =
            // 좋아요 수 데이터가 있는 경우
            if (Boolean.TRUE.equals(redisTemplate.hasKey(articleLikeCountKey))) {

            } else { // 없는 경우
                // db에서 읽어와서 redis에 저장


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 없는 경우 db에서 읽어와서 redis에 저장
        articleLikeCountRepository.incrementCountByArticleId(articleId);
        // read article
        Article article =  articleRepository.findByIdWithUser(articleId)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + articleId));

        return new ArticleResponse(article);
    }

    @Transactional
    public void deleteArticleLike(Long authorId, Long articleId, Long currentUserId) {
        // article 확인
        if (!articleRepository.existsByIdAndUserUserId(articleId, authorId)) {
            throw new DefaultErrorException(HttpStatus.NOT_FOUND, "not found article id: " + articleId);
        }

        // 좋아요 한 경우, 좋아요 삭제
        if (articleLikeRepository.existsByUserIdAndArticleId(currentUserId, articleId)) {
            articleLikeRepository.deleteByUserIdAndArticleId(currentUserId, articleId);
        }

        articleLikeCountRepository.decrementCountByArticleId(articleId);
    }

    public List<ArticleLike> findByUserIdAndArticleIdIn(Long userId, List<Long> articleIds) {
        return articleLikeRepository.findByUserIdAndArticleIdIn(userId, articleIds);
    }

    public List<ArticleLikeCount> findByArticleIdIn(List<Long> articleIds) {
        return articleLikeCountRepository.findByArticleIdIn(articleIds);
    }

    public void incrementArticleLikeCountByArticleId(Long articleId) {
        if (!articleLikeCountRepository.existsById(articleId)) {
            articleLikeCountRepository.save(new ArticleLikeCount(articleId, 1));
        } else {
            // 좋아요 수 데이터가 있는 경우
            articleLikeCountRepository.incrementCountByArticleId(articleId);
        }
    }
}
