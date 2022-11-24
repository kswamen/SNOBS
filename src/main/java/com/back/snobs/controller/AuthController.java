package com.back.snobs.controller;

import com.back.snobs.domain.snob.Role;
import com.back.snobs.payload.LoginRequest;
import com.back.snobs.payload.SignUpRequest;
import com.back.snobs.security.interceptor.CustomPreAuthorize;
import com.back.snobs.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/test")
    public String testMethod() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return "test";
    }

    @GetMapping("/testPre")
    @CustomPreAuthorize(role = Role.GRANTED_USER)
//    @PreAuthorize("hasAnyRole('GRANTED_USER', 'USER')")
    public String testMethod2() {
        return "success";
    }

    // login with password
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.authenticateUserWithEmailAndPassword(loginRequest, response);
    }

    // signup with local
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authService.registerUserWithEmailAndPassword(signUpRequest);
    }

    @GetMapping("/refreshToken")
    public String test(HttpServletResponse res, HttpServletRequest req) {
        return "test something";
    }
}