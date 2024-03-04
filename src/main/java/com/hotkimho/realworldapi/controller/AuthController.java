package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.dto.auth.CreateAccessTokenResponse;
import com.hotkimho.realworldapi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final TokenService tokenService;

    @Autowired
    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token-refresh")
    public ResponseEntity<CreateAccessTokenResponse> refreshAccessToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        String newAccessToken = tokenService.createAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
