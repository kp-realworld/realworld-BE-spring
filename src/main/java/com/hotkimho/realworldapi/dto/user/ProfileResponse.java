package com.hotkimho.realworldapi.dto.user;


import com.hotkimho.realworldapi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileResponse {
    private UserDto user;

    public ProfileResponse() {}
    public ProfileResponse(User user) {
        this.user = new UserDto(user);
    }
}
