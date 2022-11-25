package com.back.snobs.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenNotContainedException extends RuntimeException {
    public TokenNotContainedException(String message) {
        super(message);
    }

    public TokenNotContainedException(String message, Throwable cause) {
        super(message, cause);
    }
}
