package com.hotkimho.realworldapi.dto.comment;


import com.hotkimho.realworldapi.domain.Comment;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponse {
    private CommentDto comment;

    public CommentResponse() {}
    public CommentResponse(Comment comment) {
        this.comment = new CommentDto(comment);
    }
}
