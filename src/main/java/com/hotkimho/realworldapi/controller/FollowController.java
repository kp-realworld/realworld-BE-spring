package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.follow.FollowResponse;
import com.hotkimho.realworldapi.dto.user.UserDto;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.service.FollowService;
import com.hotkimho.realworldapi.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/user/follow/{followed_id}")
    @Operation(summary = "팔로우", description = "유저를 팔로우합니다.")
    @ApiResponses( value = {
                    @ApiResponse(responseCode = "200", description = "팔로우 성공", content = @Content(schema = @Schema(implementation = FollowResponse.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = DefaultErrorException.class)))
    })
    public ResponseEntity<FollowResponse> followUser(
            @PathVariable("followed_id") Long followedId) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));


        FollowResponse followResponse = followService.followUser(currentUserId, followedId);

        return ResponseEntity.status(HttpStatus.OK).body(followResponse);
    }

    @DeleteMapping("/user/follow/{followed_id}")
    @Operation(summary = "언팔로우", description = "유저를 언팔로우합니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "언팔로우 성공", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 정보", content = @Content(schema = @Schema(implementation = DefaultErrorException.class)))
    })
    public ResponseEntity<Void> unfollowUser(
            @PathVariable("followed_id") Long followedId) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        followService.unfollowUser(currentUserId, followedId);
        return ResponseEntity.noContent().build();
    }
}
