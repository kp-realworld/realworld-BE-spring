package com.hotkimho.realworldapi.service;

import com.hotkimho.realworldapi.domain.User;
import com.hotkimho.realworldapi.dto.auth.AddUserRequest;
import com.hotkimho.realworldapi.dto.user.ProfileResponse;
import com.hotkimho.realworldapi.dto.user.UpdateProfileRequest;
import com.hotkimho.realworldapi.exception.DefaultErrorException;
import com.hotkimho.realworldapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        if (userRepository.existsByEmail(requestDto.getEmail()))
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "이미 존재하는 이메일입니다.");
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new DefaultErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "이미 존재하는 사용자 이름입니다.");

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
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND,"해당 사용자가 없습니다. id= " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND,"해당 사용자가 없습니다. username=" + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND,"해당 사용자가 없습니다. email=" + email));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request, Long userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new DefaultErrorException(HttpStatus.NOT_FOUND,"해당 사용자가 없습니다. id= " + userId));

        user.updateProfile(
                request.getUsername(),
                request.getEmail(),
                request.getBio(),
                request.getProfileImage(),
                bCryptPasswordEncoder.encode(request.getPassword())
        );

        System.out.println("request username : "+ request.getUsername());
        System.out.println("username : " +user.getProfileUsername());
        return new ProfileResponse(user);
    }
}
