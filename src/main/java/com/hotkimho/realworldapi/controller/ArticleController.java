package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.ArticleResponse;
import com.hotkimho.realworldapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired()
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/article")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = articleService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/user/{author_id}/article/{article_id}")
    public ResponseEntity<ArticleResponse> getArticle(Long author_id, Long article_id) {
        Article article = articleService.findById(article_id);


    }
}
