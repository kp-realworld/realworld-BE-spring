package com.hotkimho.realworldapi.repository;

import com.hotkimho.realworldapi.domain.Follow;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // followerId와 followeeId로 팔로우 여부 확인
    boolean existsById(Long id);
    @Query("SELECT COUNT(f) > 0 FROM Follow f WHERE f.follower.id = :followerId AND f.followee.id = :followeeId")
    boolean existsByFollowerIdAndFolloweeId(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    @Transactional
    @Modifying
    // delete by followerId and followeeId
    @Query("DELETE FROM Follow f WHERE f.follower.id = :followerId AND f.followee.id = :followeeId")
    void deleteByFollowerUserIdAndFolloweeUserId(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);
}
