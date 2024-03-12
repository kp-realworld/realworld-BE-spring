package com.hotkimho.realworldapi.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotkimho.realworldapi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorDto {

    private Long authorId;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bio;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileImage;
    private boolean following;

    public AuthorDto() {}
    public AuthorDto(User user) {
        this.authorId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
        this.following = false;
    }
}

