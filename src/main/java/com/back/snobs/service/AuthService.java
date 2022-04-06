package com.back.snobs.service;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.dto.snob.*;
import com.back.snobs.error.exception.BadRequestException;
import com.back.snobs.payload.ApiResponse;
import com.back.snobs.payload.AuthResponse;
import com.back.snobs.payload.LoginRequest;
import com.back.snobs.payload.SignUpRequest;
import com.back.snobs.security.TokenProvider;
import com.back.snobs.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SnobRepository snobRepository;
    private final AuthProperties authProperties;

    public ResponseEntity<?> authenticateUserWithEmailAndPassword(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        CookieUtils.addCookie(response, "accessToken", token, authProperties.getAuth().getTokenExpirationMsec());
        CookieUtils.addCookie(response, "refreshToken", refreshToken, authProperties.getAuth().getRefreshTokenExpirationMsec());

        return ResponseEntity.ok(new AuthResponse(token, refreshToken, true));
    }

    @Transactional
    public ResponseEntity<?> registerUserWithEmailAndPassword(SignUpRequest signUpRequest) {
        if(snobRepository.existsById(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        Snob result = snobRepository.save(Snob.builder()
                .userName(signUpRequest.getName())
                .userEmail(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .birthDate(LocalDate.now())
                .loginType(LoginType.local)
                .cellPhoneCode(UUID.randomUUID().toString())
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .snobIdx(UUID.randomUUID().toString())
                .build());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getUserEmail()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }
}
