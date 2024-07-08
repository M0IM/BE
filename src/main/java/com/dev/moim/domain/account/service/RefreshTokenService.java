package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.entity.RefreshToken;
import com.dev.moim.domain.account.repository.RefreshTokenRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public void validateRefreshToken(String refreshToken) {
        RefreshToken savedRefreshToken = refreshTokenRepository
                        .findByEmail(jwtUtil.getEmail(refreshToken))
                        .orElseThrow(() -> new AuthException(NOT_CONTAIN_TOKEN));

        if (!savedRefreshToken.getToken().equals(refreshToken)) {
            throw new AuthException(NOT_EQUAL_TOKEN);
        }
    }

    @Transactional
    public void saveToken(String email, String refreshToken, Long validityMilliseconds) {
        RefreshToken newRefreshToken =
                RefreshToken.builder()
                        .email(email)
                        .token(refreshToken)
                        .expiration(validityMilliseconds)
                        .build();

        refreshTokenRepository.save(newRefreshToken);
    }

    @Transactional
    public void deleteToken(String email) {
        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new AuthException(NOT_CONTAIN_TOKEN));

        refreshTokenRepository.delete(refreshToken);
    }
}
