package com.back.snobs.error;

import com.back.snobs.error.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<CustomResponse> handleEmailDuplicateException(EmailDuplicateException ex){
        log.error("handleEmailDuplicateException", ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CellPhoneCodeDuplicateException.class)
    public ResponseEntity<CustomResponse> handleCellPhoneCodeDuplicateException(CellPhoneCodeDuplicateException ex){
        log.error("handleCellPhoneCodeDuplicateException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(BookIdDuplicateException.class)
    public ResponseEntity<CustomResponse> handleBookIdDuplicateException(BookIdDuplicateException ex){
        log.error("handleBookIdDuplicateException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ReactionDuplicateException.class)
    public ResponseEntity<CustomResponse> reactionDuplicateException(ReactionDuplicateException ex){
        log.error("reactionDuplicateException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ChatRoomDuplicationException.class)
    public ResponseEntity<CustomResponse> chatRoomDuplicationException(ChatRoomDuplicationException ex){
        log.error("chatRoomDuplicationException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(DifferentSnobException.class)
    public ResponseEntity<CustomResponse> differentSnobException(DifferentSnobException ex){
        log.error("differentSnobException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(NoDataException.class)
    public ResponseEntity<CustomResponse> handleNoDataException(NoDataException ex){
        log.error("NoDataException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(EmailVerificationFailureException.class)
    public ResponseEntity<CustomResponse> emailVerificationFailureException(EmailVerificationFailureException ex){
        log.error("EmailVerificationFailureException",ex);
        CustomResponse response = new CustomResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CustomResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.error("MaxUploadSizeExceededException", ex);
        CustomResponse response = new CustomResponse(ResponseCode.FILE_SIZE_EXCEED);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("AccessDeniedException", ex);
        CustomResponse response = new CustomResponse(ResponseCode.ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("ExpiredJwtException", ex);
        CustomResponse response = new CustomResponse(ResponseCode.TOKEN_EXPIRED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleException(Exception ex){
        log.error("handleException",ex);
        CustomResponse response = new CustomResponse(ResponseCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}