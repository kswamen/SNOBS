package com.back.snobs.util;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.error.exception.NoDataException;
import com.back.snobs.security.CustomUserDetailsService;
import com.back.snobs.security.TokenProvider;
import com.back.snobs.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenProviderTest {
    @Mock
    CustomUserDetailsService customUserDetailsService;
    @Mock
    RefreshTokenService refreshTokenService;

    @InjectMocks
    TokenProvider tokenProvider;
    @InjectMocks
    JwtTokenValidator jwtTokenValidator;

    AuthProperties authProperties;

    private final AuthProperties.Auth auth = new AuthProperties.Auth();
    String userEmail = "MySomethingEmail@hotplace.com";

    @BeforeEach
    void setup() {
        auth.setTokenSecret("A".repeat(128));
        auth.setTokenExpirationMsec(600000);
        auth.setRefreshTokenExpirationMsec(10800000);
    }

    @BeforeAll
    void setAuth() {
        authProperties = mock(AuthProperties.class);
        given(authProperties.getAuth()).willReturn(auth);
    }

    @Test
    @DisplayName("JWT 토큰을 파싱했을 때 이메일이 일치해야 한다.")
    void SameEmailCheck() {
        // given
//        given(authProperties.getAuth()).willReturn(auth);
        tokenProvider.initKey();

        // when
        String jwtToken = tokenProvider.createAccessToken(userEmail);

        // then
        assertEquals(tokenProvider.getUserEmailFromToken(jwtToken), userEmail);
    }

    @Test
    @DisplayName("만료된 토큰이 전달됨")
    void ExpiredTokenExceptionCheck() throws InterruptedException {
        // given
        auth.setTokenExpirationMsec(1);
//        given(authProperties.getAuth()).willReturn(auth);
        tokenProvider.initKey();

        // when
        String jwtToken = tokenProvider.createAccessToken(userEmail);
        Thread.sleep(10);

        // then
        assertThrows(ExpiredJwtException.class, () -> tokenProvider.validateToken(jwtToken));
    }

    @Test
    @DisplayName("위변조된 토큰이 전달됨")
    void InvalidJwtSignatureExceptionCheck() {
        // given
        auth.setTokenSecret("B".repeat(128));
//        given(authProperties.getAuth()).willReturn(auth);
        tokenProvider.initKey();

        // when
        String jwtToken = tokenProvider.createAccessToken(userEmail);
        auth.setTokenSecret("A".repeat(128));
        tokenProvider.initKey();

        // then
        assertThrows(SignatureException.class, () -> tokenProvider.validateToken(jwtToken));
    }
}