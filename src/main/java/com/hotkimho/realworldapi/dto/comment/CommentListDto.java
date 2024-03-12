package com.hotkimho.realworldapi.dto.comment;


import com.hotkimho.realworldapi.domain.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentListDto {
    private List<CommentDto> comments;

    public CommentListDto() {}
    public CommentListDto(List<Comment> comments) {
        if (comments.isEmpty()) {
            this.comments = new ArrayList<>();
            return;
        }

        this.comments = comments.stream()
                .map(CommentDto::new)
                .toList();
    }
}
