package com.back.snobs.error.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 엄밀하게 예외는 아니다.
// filter 단에서 처리하기 위해서는 exception이 처리하기 좋아서 일단 이렇게 사용한다.
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccessTokenRefreshException extends RuntimeException {
    @Getter
    @Setter
    private String email;

    public AccessTokenRefreshException(String message) {
        super(message);
    }

    public AccessTokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }
}
