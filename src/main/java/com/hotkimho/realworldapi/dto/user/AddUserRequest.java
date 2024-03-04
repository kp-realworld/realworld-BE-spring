package com.hotkimho.realworldapi.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String username;
    private String password;
    private String bio;
    private String profileImage;
}
