package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.dto.article.AddArticleRequest;
import com.hotkimho.realworldapi.dto.article.ArticleResponse;
import com.hotkimho.realworldapi.dto.article.UpdateArticleRequest;
import com.hotkimho.realworldapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable("author_id") Long author_id,
            @PathVariable("article_id") Long article_id
    ) {
        Article article = articleService.findByUserIdAndId(author_id, article_id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @PutMapping("/user/{author_id}/article/{article_id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable("author_id") Long author_id,
            @PathVariable("article_id") Long article_id,
            @RequestBody UpdateArticleRequest request
    ) {
        Article updatedArticle = articleService.update(author_id, article_id, request);
        return ResponseEntity.ok()
                .body(new ArticleResponse(updatedArticle));
    }
}
