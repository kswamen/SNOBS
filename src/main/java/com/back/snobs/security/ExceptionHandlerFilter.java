package com.back.snobs.security;

import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.BadRequestException;
import com.back.snobs.error.exception.RefreshTokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RefreshTokenExpiredException ex) {
            setErrorResponse(HttpStatus.FORBIDDEN, ResponseCode.TOKEN_EXPIRED, response);
        } catch (BadRequestException ex) {
            setErrorResponse(HttpStatus.BAD_REQUEST, ResponseCode.DATA_NOT_FOUND, response);
        } catch (Exception ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, ResponseCode.ACCESS_DENIED, response);
        }
    }

    public void setErrorResponse(HttpStatus status, ResponseCode code, HttpServletResponse response) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        CustomResponse errorResponse = new CustomResponse(code);
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
