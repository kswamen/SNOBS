package com.back.snobs.service;

import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.domain.snob.refreshToken.RefreshToken;
import com.back.snobs.domain.snob.refreshToken.RefreshTokenRepository;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.NoDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SnobRepository snobRepository;

    @Transactional
    public void saveToken(String token, String userEmail) {
        Snob snob = snobRepository.findById(userEmail).orElseThrow(() -> {
            throw new NoDataException("No User Found", ResponseCode.DATA_NOT_FOUND);
        });
        Optional<RefreshToken> temp = refreshTokenRepository.findBySnob(snob);
        RefreshToken refreshToken;
        if (temp.isPresent()) {
            refreshToken = temp.get();
            refreshToken.updateToken(token);
            refreshTokenRepository.save(refreshToken);
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .snob(snob)
                    .token(token)
                    .build());
        }
    }

    public boolean isValidRefreshToken(String userEmail, String refreshToken) {
        Snob snob = snobRepository.findById(userEmail).orElseThrow(() -> {
            throw new NoDataException("No User Found", ResponseCode.DATA_NOT_FOUND);
        });
        Optional<RefreshToken> temp = refreshTokenRepository.findBySnob(snob);
        return temp.isPresent() && temp.get().getToken().equals(refreshToken);
    }
}
