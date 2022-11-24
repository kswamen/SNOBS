package com.back.snobs.controller;

import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.error.exception.ResourceNotFoundException;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.interceptor.CustomPreAuthorize;
import com.back.snobs.security.oauth2.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final SnobRepository snobRepository;

    @GetMapping("/api/user/me")
//    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    @CustomPreAuthorize(role = Role.USER)
    public Snob getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        System.out.println(userPrincipal.toString());

        return snobRepository.findByUserEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getEmail()));
    }

    // 권한 변경
    @GetMapping("/api/user/info")
//    @PreAuthorize("hasRole('USER')")
    @CustomPreAuthorize(role = Role.USER)
    public String test(@CurrentUser UserPrincipal userPrincipal) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(a.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_GRANTED_USER"));
        Authentication newA = new UsernamePasswordAuthenticationToken(a.getPrincipal(), a.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newA);
        Authentication result = SecurityContextHolder.getContext().getAuthentication();

        return "succuess";
    }
}
