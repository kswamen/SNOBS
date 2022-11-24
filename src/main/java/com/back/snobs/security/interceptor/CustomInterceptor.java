package com.back.snobs.security.interceptor;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.BadRequestException;
import com.back.snobs.error.exception.EmailVerificationFailureException;
import com.back.snobs.error.exception.RefreshTokenExpiredException;
import com.back.snobs.security.CustomUserDetailsService;
import com.back.snobs.security.TokenProvider;
import com.back.snobs.service.RefreshTokenService;
import com.back.snobs.util.CookieUtils;
import com.back.snobs.util.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomInterceptor implements HandlerInterceptor {
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenService refreshTokenService;
//    private final CustomUserDetailsService customUserDetailsService;
//    private final AuthProperties authProperties;
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        CustomPreAuthorize customPreAuthorize = handlerMethod.getMethodAnnotation(CustomPreAuthorize.class);
        if (customPreAuthorize == null) {
            return true;
        }

        jwtTokenValidator.validate(request, response, customPreAuthorize.role());
        return true;
    }
}
