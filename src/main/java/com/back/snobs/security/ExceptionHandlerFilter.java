package com.back.snobs.security;

import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.AccessTokenExpiredException;
import com.back.snobs.error.exception.AccessTokenRefreshException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessTokenExpiredException ex) {
            setAccessTokenExpiredExceptionResponse(HttpStatus.FORBIDDEN, response);
        } catch (AccessTokenRefreshException ex) {
            setAccessTokenRefreshExceptionResponse(HttpStatus.OK, response, ex.getEmail());
        } catch (Exception ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        CustomResponse errorResponse = new CustomResponse(ResponseCode.UNAUTHORIZED);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setAccessTokenExpiredExceptionResponse(HttpStatus status, HttpServletResponse response) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        CustomResponse errorResponse = new CustomResponse(ResponseCode.TOKEN_EXPIRED);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setAccessTokenRefreshExceptionResponse(HttpStatus status, HttpServletResponse response, Object obj) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("accessToken", tokenProvider.createToken((String) obj));
        response.setStatus(status.value());
        response.setContentType("application/json");
        CustomResponse errorResponse = new CustomResponse(ResponseCode.TOKEN_EXPIRED, dataMap);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
