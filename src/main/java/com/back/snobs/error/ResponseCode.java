package com.back.snobs.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    // Success
    SUCCESS(200, "SUCCESS", "REQUEST SUCCESSED"),

    // Common
    UNAUTHORIZED(401, "UNAUTHORIZED", "UNAUTHORIZED"),
    ACCESS_DENIED(403, "ACCESS-DENIED", "ACCESS DENIED"),
    // 자기한테 권한이 없는 로그를 삭제하려고 하는 경우
    DIFFERENT_SNOB(403, "DIFFERENT-SNOB", "REQUEST FROM DIFFERENT SNOB"),
    PAGE_NOT_FOUND(404, "PAGE-NOT-FOUND","PAGE NOT FOUND"),
    TOKEN_NOT_CONTAINED(401, "TOKEN-NOT-CONTAINED", "TOKEN IS NOT CONTAINED"),
    TOKEN_EXPIRED(403, "TOKEN-EXPIRED", "TOKEN HAS BEEN EXPIRED. PLEASE LOGIN AGAIN."),
    INTER_SERVER_ERROR(500,"INTERNAL-SERVER-ERROR","INTER SERVER ERROR"),
    DATA_NOT_FOUND(404, "DATA-NOT-FOUND", "DATA NOT FOUND"),
    FILE_SIZE_EXCEED(404, "MAXIMUM-FILE-SIZE-EXCEED", "FILE SIZE EXCEEDED"),
    EMAIL_VERIFICATION_FAILED(404, "EMAIL-VERIFICATION-FAILED", "EMAIL VERIFICATION FAILED"),

    // Snob
    EMAIL_DUPLICATION(400, "EMAIL-DUPLICATE","EMAIL DUPLICATED"),
    CELLPHONECODE_DUPLICATION(400, "CELLPHONECODE-DUPLICATE", "CELLPHONECODE DUPLICATED"),
    REACTION_DUPLICATION(400, "REACTION-DUPLICATE", "REACTION DUPLICATED"),
    CHATROOM_DUPLICATION(400, "CHATROOM-DUPLICATE", "SAME CHATROOM ALREADY EXISTS"),

    // Book
    BOOK_ID_DUPLICATION(400, "BOOKID-DUPLICATE", "BOOK ID DUPLICATED"),
    ;

    private int status;
    private String errorCode;
    private String message;
}