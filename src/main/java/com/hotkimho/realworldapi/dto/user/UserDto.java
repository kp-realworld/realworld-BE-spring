package com.hotkimho.realworldapi.dto.user;

import com.hotkimho.realworldapi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String email;
    private String username;
    private String password;
    private String bio;
    private String profileImage;
    private boolean following;

    public UserDto() {}
    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
        this.following = false;
    }





}
