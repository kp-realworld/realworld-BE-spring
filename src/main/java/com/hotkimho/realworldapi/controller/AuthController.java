package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.config.jwt.JwtProperties;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.*;
import com.hotkimho.realworldapi.service.TokenService;
import com.hotkimho.realworldapi.service.UserService;
import com.hotkimho.realworldapi.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<AddUserResponse> addUser(@RequestBody AddUserRequest addUserRequest) {
        User user = userService.save(addUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AddUserResponse(user));
    }

    @PostMapping("/user/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(),
                        signinRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {

            User user = (User) principal;
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SigninResponse(
                            tokenService.createNewAccessToken(user),
                            tokenService.createRefreshToken(user),
                            user.getUserId(),
                            user.getUsername(),
                            user.getEmail()
                            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
    @GetMapping("/token-refresh")
    public ResponseEntity<CreateAccessTokenResponse> refreshAccessToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        String token = AuthUtil.getAccessToken(refreshToken);
        String newAccessToken = tokenService.createAccessToken(token);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
