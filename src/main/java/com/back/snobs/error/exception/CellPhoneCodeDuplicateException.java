package com.back.snobs.error.exception;

import com.back.snobs.error.ResponseCode;
import lombok.Getter;

@Getter
public class CellPhoneCodeDuplicateException extends RuntimeException {
    private final ResponseCode errorCode;

    public CellPhoneCodeDuplicateException(String message, ResponseCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
