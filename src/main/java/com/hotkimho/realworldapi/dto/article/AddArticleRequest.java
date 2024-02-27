package com.hotkimho.realworldapi.dto.article;

import com.hotkimho.realworldapi.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;
    private String description;
    private String body;
    private String[] tagList;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .description(description)
                .body(body)
                .build();
    }
}
