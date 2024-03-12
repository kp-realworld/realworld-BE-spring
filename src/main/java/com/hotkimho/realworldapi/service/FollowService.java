package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Follow;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.user.UserDto;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.FollowRepository;
import com.hotkimho.realworldapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public UserDto followUser(Long followerId, Long followeeId) {
        // 이미 팔로우 했는지 확인
        if (followRepository.existsByFollowerUserIdAndFolloweeUserId(followerId, followeeId)) {
            User followee = userRepository.findById(followeeId)
                    .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "팔로우 대상이 존재하지 않습니다."));
            return new UserDto(followee, true);
        }

        User follower = new User(followerId);
        User followee = new User(followeeId);

        try {
            Follow savedFollow = followRepository.save(new Follow(follower, followee));
            return new UserDto(savedFollow.getFollowee(), true);

        }
        catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "팔로우에 실패했습니다.");
        }
    }

    public void unfollowUser(Long followerId, Long followeeId) {
        try {
            followRepository.deleteByFollowerUserIdAndFolloweeUserId(followerId, followeeId);
        } catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "팔로우 취소에 실패했습니다.");
        }
    }
}
