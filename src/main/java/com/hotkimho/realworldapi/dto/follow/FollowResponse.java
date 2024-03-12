package com.hotkimho.realworldapi.dto.follow;

import com.hotkimho.realworldapi.domain.Follow;

public class FollowResponse {
    private FollowDto user;

    public FollowResponse() {}
    public FollowResponse(FollowDto follow) {
        this.user = new FollowDto(, true);
    }
}
