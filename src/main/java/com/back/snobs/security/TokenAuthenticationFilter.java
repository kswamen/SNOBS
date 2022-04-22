package com.back.snobs.security;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.BookIdDuplicateException;
import com.back.snobs.service.RefreshTokenService;
import com.back.snobs.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthProperties authProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//            const defaults = {
//                    headers: headers,
//                    credentials: 'include' // react 쪽 cors 설정 잊지 말자.
//            };
            Optional<Cookie> accessTokenCookie = CookieUtils.getCookie(request,"accessToken");
            Optional<Cookie> refreshTokenCookie = CookieUtils.getCookie(request,"refreshToken");
            if (accessTokenCookie.isPresent()) {
                String accessToken = accessTokenCookie.get().getValue();
                if (tokenProvider.validateToken(accessToken)) {
                    String email = tokenProvider.getUserEmailFromToken(accessToken);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // access token 만기된 경우
                else {
                    if (refreshTokenCookie.isPresent()) {
                        String refreshToken = refreshTokenCookie.get().getValue();
                        if (tokenProvider.validateToken(refreshToken)) {
                            String email = tokenProvider.getUserEmailFromToken(refreshToken);
                            if (refreshTokenService.isValidRefreshToken(email, refreshToken)) {
                                String newToken = tokenProvider.createToken(email);
                                CookieUtils.addCookie(response, "accessToken", newToken, authProperties.getAuth().getTokenExpirationMsec());
                                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        } else {
                            throw new RuntimeException();
                        }
                    }
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            throw ex;
        }
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
