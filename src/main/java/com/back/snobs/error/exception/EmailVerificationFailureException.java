package com.back.snobs.error.exception;

import com.back.snobs.error.ResponseCode;
import lombok.Getter;

@Getter
public class EmailVerificationFailureException extends RuntimeException {
    private final ResponseCode errorCode;

    public EmailVerificationFailureException(String message, ResponseCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
