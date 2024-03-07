package com.hotkimho.realworldapi.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleTag;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddArticleRequest {
    private String title;
    private String description;
    private String body;

    @JsonProperty("tag_list")
    private List<String> tagList;

    private User author;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .description(description)
                .body(body)
                .user(author)
                .build();
    }

    public List<ArticleTag> toArticleTagsEntity(Article article) {
        if (tagList == null) {
            return List.of();
        }

        return tagList.stream()
                .map(tag -> ArticleTag.builder()
                        .article(article)
                        .tag(tag)
                        .build())
                .toList();
    }
}
