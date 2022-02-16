package com.back.snobs.dto.snob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("MALE", "남성"),
    FEMALE("FEMALE", "여성");

    private final String key;
    private final String sex;
}
