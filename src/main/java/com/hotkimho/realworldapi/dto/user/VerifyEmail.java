package com.hotkimho.realworldapi.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmail {
    @NotBlank
    @Email
    private String email;

    public VerifyEmail() {}
    public VerifyEmail(String email) {
        this.email = email;
    }
}
