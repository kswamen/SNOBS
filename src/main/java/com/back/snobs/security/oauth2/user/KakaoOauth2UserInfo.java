package com.back.snobs.security.oauth2.user;

import java.util.Map;

public class KakaoOauth2UserInfo extends OAuth2UserInfo {

    public KakaoOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    @Override
    public String getName() {

        return (String) kakaoProfile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) kakaoProfile.get("profile_image_url");
    }
}