package com.hotkimho.realworldapi.dto.comment;

import com.hotkimho.realworldapi.domain.Article;
import com.hotkimho.realworldapi.domain.Comment;
import com.hotkimho.realworldapi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {
    private String body;

    public Comment toEntity(Article article, User user) {
        return Comment.builder()
                .body(body)
                .article(article)
                .user(user)
                .build();
    }
}
