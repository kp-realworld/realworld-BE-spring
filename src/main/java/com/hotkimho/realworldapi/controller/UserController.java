package com.hotkimho.realworldapi.controller;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.AddUserRequest;
import com.hotkimho.realworldapi.dto.auth.AddUserResponse;
import com.hotkimho.realworldapi.dto.common.ErrorResponse;
import com.hotkimho.realworldapi.dto.user.ProfileResponse;
import com.hotkimho.realworldapi.dto.user.UpdateProfileRequest;
import com.hotkimho.realworldapi.dto.user.VerifyEmail;
import com.hotkimho.realworldapi.dto.user.VerifyUsername;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.UserService;
import com.hotkimho.realworldapi.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
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


    @GetMapping("/user/{user_id}/profile")
    @Operation(summary = "프로필 조회", description = "유저의 프로필을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "프로필 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable("user_id") Long userId) {
        User user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ProfileResponse(user));
    }

    @PutMapping("/user/profile")
    @Operation(summary = "프로필 수정", description = "유저의 프로필을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "프로필 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<ProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        ProfileResponse profileResponse = userService.updateProfile(request, currentUserId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(profileResponse);
    }
}
