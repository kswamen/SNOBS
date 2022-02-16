package com.back.snobs.security.oauth2.user;

import com.back.snobs.dto.snob.LoginType;
import com.back.snobs.error.exception.OAuth2AuthenticationProcessingException;

import java.security.AuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(LoginType.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(LoginType.kakao.toString())) {
            return new KakaoOauth2UserInfo(attributes);
        }
        else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}