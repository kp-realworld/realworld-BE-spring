package com.hotkimho.realworldapi.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleTag;
import com.hotkimho.realworldapi.dto.user.AuthorDto;
import com.hotkimho.realworldapi.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ArticleDto {

        private Long articleId;
        private AuthorDto Author;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<String> tagList;

        private String title;
        private String description;
        private String body;
        private int favoriteCount;

        @JsonProperty("is_favorited")
        private boolean favorited;
        private LocalDateTime createdAt;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDateTime updatedAt;

        public ArticleDto() {}

        public ArticleDto(Article article) {
                this.articleId = article.getId();
                this.Author = new AuthorDto(article.getUser());
                this.title = article.getTitle();
                this.description = article.getDescription();
                this.body = article.getBody();
                this.favoriteCount = article.getFavoriteCount();
                this.createdAt = article.getCreatedAt();
                this.updatedAt = article.getUpdatedAt();
                this.tagList = article.getArticleTags()
                        .stream()
                        .map(ArticleTag::getTag)
                        .collect(Collectors.toList());
        }
}
