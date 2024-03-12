package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerUserIdAndFolloweeUserId(Long followerId, Long followeeId);
    void deleteByFollowerUserIdAndFolloweeUserId(Long followerId, Long followeeId);
}
