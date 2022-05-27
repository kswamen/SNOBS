package com.back.snobs.domain.snob;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SnobDto {
    private String nickName;
    private String cellPhoneCode;
    private String password;
    private LoginType loginType;
    private String userName;
    private String birthDate;
    private Gender gender;
    private String address;
    private String selfIntroduction;
    private String mainProfileImageUrl;
    private Role role;

//    public Snob toEntity() {
//        return Snob.builder()
//                .cellPhoneCode(cellPhoneCode)
//                .password(password)
//                .loginType(loginType)
//                .role(role)
//                .userName(userName)
//                .birthDate(birthDate)
//                .gender(gender)
//                .address(address)
//                .selfIntroduction(selfIntroduction)
//                .build();
//    }
}
