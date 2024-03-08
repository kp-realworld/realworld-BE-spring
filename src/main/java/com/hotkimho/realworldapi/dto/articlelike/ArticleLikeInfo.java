package com.hotkimho.realworldapi.dto.articlelike;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticleLikeInfo {
    private int likeCount;
    private boolean favorited;

    public ArticleLikeInfo() {}

    @Builder
    public ArticleLikeInfo(int likeCount, boolean favorited) {
        this.likeCount = likeCount;
        this.favorited = favorited;
    }
}
