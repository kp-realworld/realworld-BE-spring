package com.hotkimho.realworldapi.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUsername {
    private String username;

    public VerifyUsername() {}
    public VerifyUsername(String username) {
        this.username = username;
    }
}
