package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.AddUserRequest;
import com.hotkimho.realworldapi.dto.auth.AddUserResponse;
import com.hotkimho.realworldapi.dto.common.ErrorResponse;
import com.hotkimho.realworldapi.dto.user.VerifyEmail;
import com.hotkimho.realworldapi.dto.user.VerifyUsername;
import com.hotkimho.realworldapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/user/verify-email")
    @Operation(summary = "이메일 중복 확인", description = "이메일 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공", content = @Content(schema = @Schema(implementation = VerifyEmail.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이메일이 존재하는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<VerifyEmail> verifyEmail(@RequestBody VerifyEmail verifyEmail) {
        if (userService.existsByEmail(verifyEmail.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new VerifyEmail(verifyEmail.getEmail()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new VerifyEmail(verifyEmail.getEmail()));
    }


    @PostMapping("/user/verify-username")
    @Operation(summary = "유저네임 중복 확인", description = "유저네임 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저네임 중복 확인 성공", content = @Content(schema = @Schema(implementation = VerifyUsername.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "유저네임이 존재하는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<VerifyUsername> verifyUsername(@RequestBody VerifyUsername verifyUsername) {
        System.out.println("verifyEmail.getEmail() : "+verifyUsername.getUsername());
        if (userService.existsByUsername(verifyUsername.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new VerifyUsername(verifyUsername.getUsername()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new VerifyUsername(verifyUsername.getUsername()));
    }
}
