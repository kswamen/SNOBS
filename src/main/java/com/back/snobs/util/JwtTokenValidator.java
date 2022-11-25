package com.back.snobs.util;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.error.exception.TokenNotContainedException;
import com.back.snobs.security.CustomUserDetailsService;
import com.back.snobs.security.TokenProvider;
import com.back.snobs.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtTokenValidator {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final AuthProperties authProperties;

    public void validate(HttpServletRequest request, HttpServletResponse response, Role role) {
        Optional<Cookie> accessTokenCookie = CookieUtils.getCookie(request,"accessToken");
        Optional<Cookie> refreshTokenCookie = CookieUtils.getCookie(request,"refreshToken");
        if (accessTokenCookie.isPresent()) {
            String accessToken = accessTokenCookie.get().getValue();
            try {
                tokenProvider.validateToken(accessToken, role);
                String email = tokenProvider.getUserEmailFromToken(accessToken);
                authenticate(email, request);
            } catch (ExpiredJwtException ex) {
                // access token 만기된 경우
                if (refreshTokenCookie.isPresent()) {
                    String refreshToken = refreshTokenCookie.get().getValue();
                    tokenProvider.validateToken(refreshToken, role);
                    String email = tokenProvider.getUserEmailFromToken(refreshToken);
                    if (refreshTokenService.isValidRefreshToken(email, refreshToken)) {
                        String newToken = tokenProvider.createAccessToken(email);
                        CookieUtils.addCookie(response, "accessToken", newToken, authProperties.getAuth().getTokenExpirationMsec());
                        authenticate(email, request);
                    }
                }

                // token 존재 안함(잘못된 요청)
                else {
                    throw new TokenNotContainedException("Token is NOT Contained.");
                }
            }
        } else {
            throw new TokenNotContainedException("Token is NOT Contained.");
        }
    }

    private void authenticate(String email, HttpServletRequest request) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    private String getAccessTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("AccessToken");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }
//
//    private String getRefreshTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("RefreshToken");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }
}
