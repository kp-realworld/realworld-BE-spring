package com.hotkimho.realworldapi.dto.follow;

import com.hotkimho.realworldapi.domain.Follow;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FollowResponse {
    private FollowDto user;

    public FollowResponse() {}
    public FollowResponse(User user, boolean following) {
        this.user = new FollowDto(user, following);
    }
}
