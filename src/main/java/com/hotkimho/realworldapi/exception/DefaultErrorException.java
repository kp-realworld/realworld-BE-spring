package com.hotkimho.realworldapi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DefaultErrorException extends RuntimeException{

    private final String message;
    private final HttpStatus code;

    public DefaultErrorException(HttpStatus code, String message) {
        super(message);

        System.out.println("code : "+code.value());
        System.out.println("message : "+ message);
        this.message = message;
        this.code = code;
    }
}
