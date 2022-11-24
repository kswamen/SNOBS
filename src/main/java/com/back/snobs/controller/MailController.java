package com.back.snobs.controller;

import com.back.snobs.domain.snob.Role;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.interceptor.CustomPreAuthorize;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailController {
    private final MailService mailService;

    // Send Email to User
    @GetMapping("/verification")
    @CustomPreAuthorize(role = Role.USER)
//    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    public ResponseEntity<CustomResponse> sendMail(@CurrentUser UserPrincipal userPrincipal) {
        return mailService.mailSend(userPrincipal.getEmail());
    }

    @PostMapping("/verification")
    @CustomPreAuthorize(role = Role.USER)
//    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    public ResponseEntity<CustomResponse> checkCode(@CurrentUser UserPrincipal userPrincipal, String verificationCode) {
        return mailService.checkVerificationCode(userPrincipal.getEmail(), verificationCode);
    }
}
