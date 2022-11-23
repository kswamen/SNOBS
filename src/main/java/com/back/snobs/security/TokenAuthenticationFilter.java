package com.back.snobs.security;

import com.back.snobs.util.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//            const defaults = {
//                    headers: headers,
//                    credentials: 'include' // react 쪽 cors 설정 잊지 말자.
//            };
//            authenticate("kimseokwon95@gmail.com", request);
//            filterChain.doFilter(request, response);
            jwtTokenValidator.validate(request, response);
            filterChain.doFilter(request, response);
        }
        catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            throw ex;
        }
    }
}
