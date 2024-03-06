package com.hotkimho.realworldapi.repository;


import com.hotkimho.realworldapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
