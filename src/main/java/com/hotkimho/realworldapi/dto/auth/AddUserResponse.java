package com.hotkimho.realworldapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotkimho.realworldapi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AddUserResponse {

    private String email;
    private String username;
    private String bio;
    private String profileImage;
    private String token;

    public AddUserResponse(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
    }
}
