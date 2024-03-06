package com.hotkimho.realworldapi.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail {
    private final String message;
    private final int code;

    public ErrorDetail(int code, String message) {
        this.message = message;
        this.code = code;
    }
}
