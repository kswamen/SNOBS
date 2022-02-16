package com.back.snobs.error.exception;

import com.back.snobs.error.ResponseCode;
import lombok.Getter;

@Getter
public class BookIdDuplicateException extends RuntimeException {
    private final ResponseCode errorCode;

    public BookIdDuplicateException(String message, ResponseCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
