package com.hotkimho.realworldapi.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.ArticleTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateArticleRequest {
    private String title;
    private String description;
    private String body;
    @JsonProperty("tag_list")
    private List<String> tagList;

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
