package com.back.snobs.domain.snob.refreshToken;

import com.back.snobs.domain.snob.Snob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findBySnob(Snob snob);
}