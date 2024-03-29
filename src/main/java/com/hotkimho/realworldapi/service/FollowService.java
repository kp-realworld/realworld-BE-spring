package com.hotkimho.realworldapi.service;


import com.hotkimho.realworldapi.domain.Follow;
import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.follow.FollowResponse;
import com.hotkimho.realworldapi.dto.user.UserDto;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.FollowRepository;
import com.hotkimho.realworldapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Transactional
    public FollowResponse followUser(Long followerId, Long followeeId) {
        // 이미 팔로우 했는지 확인
        if (followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            User followee = userRepository.findById(followeeId)
                    .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "팔로우 대상이 존재하지 않습니다."));
            return new FollowResponse(followee, true);
        }

        User follower = new User(followerId);
        User followee = new User(followeeId);

        try {
            Follow savedFollow = followRepository.save(new Follow(follower, followee));
            User followedUser = userRepository.findById(followeeId)
                    .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND, "팔로우 대상이 존재하지 않습니다."));
            return new FollowResponse(followedUser, true);

        }
        catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "팔로우에 실패했습니다.");
        }
    }

    public void unfollowUser(Long followerId, Long followeeId) {
        if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            return;
        }

        try {
            followRepository.deleteByFollowerUserIdAndFolloweeUserId(followerId, followeeId);
        } catch (Exception e) {
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "팔로우 취소에 실패했습니다. : "+ e.getMessage());
        }
    }

    public boolean isFollowing(Long currentUserId, Long targetUserId) {
        if (currentUserId == 0 || targetUserId == 0) {
            return false;
        }

        return followRepository.existsByFollowerIdAndFolloweeId(currentUserId, targetUserId);
    }

    public List<Follow> getFollowers(Long currentUserId, List<Long> followeeId) {
        if (currentUserId == 0 || followeeId.isEmpty()) {
            return List.of();
        }
        return followRepository.findByFollowerIdAndFolloweeIdIn(currentUserId, followeeId);
    }
}
