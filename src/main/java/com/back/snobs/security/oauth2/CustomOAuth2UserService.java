package com.back.snobs.security.oauth2;

import com.back.snobs.domain.snob.*;
import com.back.snobs.error.exception.OAuth2AuthenticationProcessingException;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.oauth2.user.OAuth2UserInfo;
import com.back.snobs.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final SnobRepository snobRepository;

    // loadUser
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // access token을 바탕으로 oauth provider로부터 사용자 정보를 받아옴.
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Snob> userOptional = snobRepository.findById(oAuth2UserInfo.getEmail());
        Snob user;
        // 이미 존재하는 유저일 경우, OAuth2 정보에 따라 데이터베이스 업데이트
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getLoginType().equals(LoginType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getLoginType() + " account. Please use your " + user.getLoginType() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            // 신규 유저는 새로 등록
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private Snob registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Snob user = Snob.builder()
                .userEmail(oAuth2UserInfo.getEmail())
                .userName(oAuth2UserInfo.getName())
                .loginType(LoginType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .birthDate(LocalDate.now())
                .cellPhoneCode(UUID.randomUUID().toString())
                .gender(Gender.MALE)
                .role(Role.USER)
                .snobIdx(UUID.randomUUID().toString())
                .build();

        return snobRepository.save(user);
    }

    private Snob updateExistingUser(Snob existingUser, OAuth2UserInfo oAuth2UserInfo) {
//        existingUser.SnobUpdate("SomeWhere", "");
        return snobRepository.save(existingUser);
    }

}
