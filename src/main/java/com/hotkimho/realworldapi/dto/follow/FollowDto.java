package com.hotkimho.realworldapi.dto.follow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotkimho.realworldapi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FollowDto {

    private Long userId;
    private String email;
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bio;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileImage;
    private boolean following;

    public FollowDto() {}

    public FollowDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
    }

    public FollowDto(User user, boolean following) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
        this.following = following;
    }
}
