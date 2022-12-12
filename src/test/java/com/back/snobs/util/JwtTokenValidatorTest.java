package com.back.snobs.util;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.error.exception.TokenNotContainedException;
import com.back.snobs.security.CustomUserDetailsService;
import com.back.snobs.security.TokenProvider;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.service.RefreshTokenService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static com.back.snobs.other.CreateDummyData.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtTokenValidatorTest {
    // @Mock으로 하면 NPE 뜰 때가 있는데,
    // mock()으로 만들면 안 뜸. 이유 조사 필요

    RefreshTokenService refreshTokenService;
    AuthProperties authProperties;
    CustomUserDetailsService customUserDetailsService;
    TokenProvider tokenProvider;
    private final AuthProperties.Auth auth = new AuthProperties.Auth();

    @InjectMocks
    JwtTokenValidator jwtTokenValidator;

    private MockHttpServletRequest servletRequest;
    private MockHttpServletResponse servletResponse;
    private final String userEmail = "myEmail@naver.com";
    private final UserPrincipal userPrincipal = UserPrincipal.create(getOneSnob(LoginType.local, Role.GRANTED_USER));

    @BeforeAll
    void setAuth() {
        authProperties = mock(AuthProperties.class);
        tokenProvider = mock(TokenProvider.class);
        customUserDetailsService = mock(CustomUserDetailsService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        given(authProperties.getAuth()).willReturn(auth);
        given(customUserDetailsService.loadUserByUsername(any())).willReturn(userPrincipal);
        given(tokenProvider.getUserEmailFromToken(any())).willReturn(userEmail);
        given(refreshTokenService.isValidRefreshToken(any(), any())).willReturn(true);
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        auth.setTokenSecret("A".repeat(128));
        auth.setTokenExpirationMsec(600000);
        auth.setRefreshTokenExpirationMsec(10800000);

        servletRequest = new MockHttpServletRequest();
        servletResponse = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("AccessToken 과 RefreshToken 이 둘 다 존재하지 않음")
    void NoTokenContained() {
        // given

        // when

        // then
        assertThrows(TokenNotContainedException.class, () -> {
            jwtTokenValidator.validate(servletRequest, servletResponse, Role.GRANTED_USER);
        });
    }

    @Test
    @DisplayName("AccessToken 존재 시 예외 전달하지 않음")
    void AccessTokenContained() {
        // given
        Cookie[] cookies = new Cookie[]{
                new Cookie("accessToken", createJwtToken(userEmail)),
                new Cookie("refreshToken", createJwtToken(userEmail))
        };
        servletRequest.setCookies(cookies);

        given(tokenProvider.validateToken(any(), any())).willReturn(true);

        // when

        // then
        assertDoesNotThrow(() -> jwtTokenValidator.validate(servletRequest, servletResponse, Role.GRANTED_USER));
    }

    @Test
    @DisplayName("AccessToken 만료, RefreshToken 유효")
    void AccessTokenRefreshed() throws InterruptedException {
        // given
        String prevJwtToken = createSoonExpireJwtToken(userEmail);
        Cookie[] cookies = new Cookie[]{
                new Cookie("accessToken", prevJwtToken),
                new Cookie("refreshToken", createJwtToken(userEmail))
        };
        Thread.sleep(10);
        servletRequest.setCookies(cookies);
        // accessToken, refreshToken 순서
        given(tokenProvider.validateToken(any(), any())).willReturn(false, true);

        // when

        // then
        assertDoesNotThrow(() -> jwtTokenValidator.validate(servletRequest, servletResponse, Role.GRANTED_USER));
    }
}