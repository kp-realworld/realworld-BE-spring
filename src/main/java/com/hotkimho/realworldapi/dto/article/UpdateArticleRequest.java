package com.hotkimho.realworldapi.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateArticleRequest {
    private String title;
    private String description;
    private String body;
    private String[] tagList;
}
