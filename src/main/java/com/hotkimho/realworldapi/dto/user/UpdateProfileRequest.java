package com.hotkimho.realworldapi.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/*
{
  "bio": "string",
  "email": "string",
  "password": "string",
  "profile_image": "string",
  "username": "string"
}
 */
@Setter
@Getter
public class UpdateProfileRequest {

    // nullable bio
    private String bio;

    @NotNull
    private String email;
    @NonNull
    private String password;
    private String profileImage;
    private String username;

    public UpdateProfileRequest() {}


}
