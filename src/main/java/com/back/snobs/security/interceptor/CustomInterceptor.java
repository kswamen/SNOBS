package com.back.snobs.security.interceptor;

import com.back.snobs.util.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class CustomInterceptor implements HandlerInterceptor {
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
        SecurityContext sc = SecurityContextHolder.getContext();

        jwtTokenValidator.validate(request, response, customPreAuthorize.role());
        return true;
    }
}
