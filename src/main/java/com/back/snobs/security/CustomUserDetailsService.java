package com.back.snobs.security;

import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final SnobRepository snobRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Snob snob = snobRepository.findById(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email :" + email));
        return UserPrincipal.create(snob);
    }
}
