package com.hotkimho.realworldapi.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotkimho.realworldapi.domain.Comment;
import com.hotkimho.realworldapi.dto.user.AuthorDto;
import com.hotkimho.realworldapi.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDto {
    private Long id;
    private String body;

    @JsonProperty("article_id")
    private Long articleId;
    private AuthorDto Author;

    private LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    public CommentDto() {}

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.articleId = comment.getArticle().getId();
        this.Author = new AuthorDto(comment.getUser());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
