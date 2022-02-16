package com.back.snobs.dto.snob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GRANTED_USER("ROLE_GRANTED_USER", "정보 입력된 사용자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
