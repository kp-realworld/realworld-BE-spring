package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.*;
import com.hotkimho.realworldapi.dto.common.ErrorResponse;
import com.hotkimho.realworldapi.dto.user.VerifyEmail;
import com.hotkimho.realworldapi.service.TokenService;
import com.hotkimho.realworldapi.service.UserService;
import com.hotkimho.realworldapi.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    //     @ content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddUserResponse.class))),
    @PostMapping("/user/signup")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = AddUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "중복된 회원 존재" , content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<AddUserResponse> addUser(@RequestBody AddUserRequest addUserRequest) {
            User user = userService.save(addUserRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AddUserResponse(user));
    }

    @PostMapping("/user/signin")
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SigninResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "402", description = "로그인 실패(비밀번호가 유효하지 않음)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
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




    @GetMapping("/heartbeat")
    public String heartbeat() {

        return "I'm alive!";
    }
}
