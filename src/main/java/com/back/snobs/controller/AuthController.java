package com.back.snobs.controller;

import com.back.snobs.payload.LoginRequest;
import com.back.snobs.payload.SignUpRequest;
import com.back.snobs.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

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