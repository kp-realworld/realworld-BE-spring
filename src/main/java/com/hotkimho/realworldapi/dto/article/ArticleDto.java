package com.hotkimho.realworldapi.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleTag;
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
        private UserDto Author;
        private List<String> tagList;

        private String title;
        private String description;
        private String body;
        private int favoriteCount;
        private LocalDateTime createdAt;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDateTime updatedAt;

        public ArticleDto() {
        }

        public ArticleDto(Article article) {
                this.articleId = article.getId();
                this.Author = new UserDto(article.getUser());
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
