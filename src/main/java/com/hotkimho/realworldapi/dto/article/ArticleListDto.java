package com.hotkimho.realworldapi.dto.article;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ArticleListDto {
    private List<ArticleDto> articles;

    public ArticleListDto() {}
    public ArticleListDto(List<ArticleDto> articles) {
        this.articles = articles;
    }
}
