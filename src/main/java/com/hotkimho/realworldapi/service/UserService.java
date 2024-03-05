package com.hotkimho.realworldapi.service;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.AddUserRequest;
import com.hotkimho.realworldapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User save(AddUserRequest requestDto) {
        return userRepository.save(User.builder()
                .email(requestDto.getEmail())
                .username(requestDto.getUsername())
                .bio(requestDto.getBio())
                .profileImage(requestDto.getProfileImage())
                .password(bCryptPasswordEncoder.encode(requestDto.getPassword()))
                .build());
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. username=" + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. email=" + email));
    }

}
