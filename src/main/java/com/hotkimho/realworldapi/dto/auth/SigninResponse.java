package com.hotkimho.realworldapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SigninResponse {
    // access token, refresh token
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
}
