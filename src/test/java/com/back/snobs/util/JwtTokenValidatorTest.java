package com.back.snobs.util;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.error.exception.BadRequestException;
import com.back.snobs.security.CustomUserDetailsService;
import com.back.snobs.security.TokenProvider;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.service.RefreshTokenService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static com.back.snobs.other.CreateDummyData.createJwtToken;
import static com.back.snobs.other.CreateDummyData.getOneSnob;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtTokenValidatorTest {
    // @Mock으로 하면 NPE 뜰 때가 있는데,
    // mock()으로 만들면 안 뜸. 이유 조사 필요

    @Mock
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
        given(authProperties.getAuth()).willReturn(auth);
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        auth.setTokenSecret("A".repeat(128));
        auth.setTokenExpirationMsec(600000);
        auth.setRefreshTokenExpirationMsec(10800000);

        servletRequest = new MockHttpServletRequest();
        servletResponse = new MockHttpServletResponse();
        if (testInfo.getTestMethod().get().getName().equals("NoTokenContained")) return;

        Cookie[] cookies = new Cookie[]{
                new Cookie("accessToken", createJwtToken(userEmail)),
                new Cookie("refreshToken", createJwtToken(userEmail))
        };

        servletRequest.setCookies(cookies);
    }

    @Test
    @Order(2)
    @DisplayName("AccessToken 과 RefreshToken 이 둘 다 존재하지 않음")
    void NoTokenContained() {
        // given

        // when

        // then
        assertThrows(BadRequestException.class, () -> {
            jwtTokenValidator.validate(servletRequest, servletResponse, Role.GRANTED_USER);
        });
    }

    @Test
    @Order(3)
    @DisplayName("AccessToken 만 존재, 유효한 토큰")
    void AccessTokenContained() {
        // given
        given(customUserDetailsService.loadUserByUsername(any())).willReturn(userPrincipal);
        given(tokenProvider.validateToken(any(), any())).willReturn(true);
        given(tokenProvider.getUserEmailFromToken(any())).willReturn(userEmail);

        // when

        // then
        assertDoesNotThrow(() -> jwtTokenValidator.validate(servletRequest, servletResponse, Role.GRANTED_USER));
    }
}