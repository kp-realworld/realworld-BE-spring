package com.hotkimho.realworldapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DefaultErrorException.class)
    public ResponseEntity<?> handleDefaultErrorException(DefaultErrorException ex) {
        logger.info("Throwing DefaultErrorException for duplicated email/username");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", Map.of(
                "code", ex.getCode().value(),
                "message", ex.getMessage()
        ));
        System.out.println("code2 : "+ex.getCode().value());
        System.out.println("message 2: "+ ex.getMessage());
        return new ResponseEntity<>(body, ex.getCode());
    }
}