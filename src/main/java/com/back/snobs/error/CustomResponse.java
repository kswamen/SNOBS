package com.back.snobs.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponse {
    private int status;
    private String message;
    private String code;
    private Object payload;

    public CustomResponse(ResponseCode responseCode){
        this.status = responseCode.getStatus();
        this.message = responseCode.getMessage();
        this.code = responseCode.getErrorCode();
    }

    public CustomResponse(ResponseCode responseCode, Object obj) {
        this.status = responseCode.getStatus();
        this.message = responseCode.getMessage();
        this.code = responseCode.getErrorCode();
        this.payload = obj;
    }
}