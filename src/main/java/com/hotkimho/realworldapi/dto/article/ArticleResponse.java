package com.hotkimho.realworldapi.dto.article;

import com.hotkimho.realworldapi.domain.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {

    private Long id;
    private String title;
    private String description;
    private String body;
    private String[] tagList;
    private boolean favorited;
    private int favoritesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.favorited = false;
        this.favoritesCount = article.getFavoriteCount();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();

    }
}
