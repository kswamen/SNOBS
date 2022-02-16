package com.back.snobs.error.exception;

import com.back.snobs.error.ResponseCode;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class NoDataException extends RuntimeException {
    private final ResponseCode errorCode;

    public NoDataException(String message, ResponseCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
