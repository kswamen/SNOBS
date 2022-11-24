package com.back.snobs.domain.snob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GRANTED_USER("ROLE_GRANTED_USER", "정보 입력된 사용자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

    // 일반 유저가 정보 입력된 유저 역할로 들어올 경우만 잘못됨
    public static boolean roleCheck(Role role1, Role role2) {
        return role1 != Role.USER || role2 != Role.GRANTED_USER;
    }
}
