package com.hotkimho.realworldapi.util;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleLike;
import com.hotkimho.realworldapi.domain.ArticleLikeCount;
import com.hotkimho.realworldapi.domain.Follow;
import com.hotkimho.realworldapi.dto.article.ArticleDto;
import com.hotkimho.realworldapi.dto.article.ArticleListDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class Convert {

    private Convert() {
        throw new IllegalStateException("Utility class");
    }

    public static ArticleListDto convertToArticleListDtoWithLikeInfo(
            List<Article> articles,
            List<ArticleLike> articleLikes,
            List<ArticleLikeCount> articleLikeCounts,
            List<Follow> follows
    ) {
        List<ArticleDto> articleListDto = new ArrayList<>();

        // article like map
        Map<Long, Boolean> articleLikeMap = new HashMap<>();
        for (ArticleLike articleLike : articleLikes) {
            articleLikeMap.put(articleLike.getArticleId(), true);
        }

        // article like count map
        Map<Long, Integer> articleLikeCountMap = new HashMap<>();
        for (ArticleLikeCount articleLikeCount : articleLikeCounts) {
            articleLikeCountMap.put(articleLikeCount.getArticleId(), articleLikeCount.getCount());
        }

        Map<Long, Boolean> followMap = new HashMap<>();
        for (Follow follow : follows) {
            followMap.put(follow.getFollowee().getUserId(), true);
        }

        for (Article article : articles) {
            ArticleDto articleDto = new ArticleDto(article);

            articleDto.setFavorited(articleLikeMap.get(article.getId()) != null);
            articleDto.setFavoriteCount(articleLikeCountMap.getOrDefault(article.getId(), 0));
            articleListDto.add(articleDto);

            articleDto.getAuthor().setFollowing(followMap.get(articleDto.getAuthor().getAuthorId()) != null);
        }

        return new ArticleListDto(articleListDto);
    }

}
