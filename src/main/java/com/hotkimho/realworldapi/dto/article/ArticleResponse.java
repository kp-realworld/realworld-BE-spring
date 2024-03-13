package com.hotkimho.realworldapi.dto.article;

import com.hotkimho.realworldapi.domain.Article;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleResponse {
    private ArticleDto article;

    public ArticleResponse() {}
    public ArticleResponse(Article article) {
        this.article = new ArticleDto(article);
    }
}
