package com.hotkimho.realworldapi.dto.auth;


import lombok.Getter;

@Getter
public class CreateAccessTokenResponse {
    private String accessToken;


    public CreateAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
