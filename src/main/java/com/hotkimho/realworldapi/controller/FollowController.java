package com.hotkimho.realworldapi.controller;


import com.hotkimho.realworldapi.domain.User;
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

@RestController
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/user/follow/{followed_id}")
    public ResponseEntity<UserDto> followUser(
            @PathVariable("followed_id") Long followedId) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));


        UserDto userDto = followService.followUser(currentUserId, followedId);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/user/follow/{followee_id}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable("followed_id") Long followedId) {
        Long currentUserId = AuthUtil.getCurrentUserId()
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."));

        followService.unfollowUser(currentUserId, followedId);
        return ResponseEntity.noContent().build();
    }
}
