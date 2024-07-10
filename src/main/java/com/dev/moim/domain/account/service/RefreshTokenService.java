package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.entity.RefreshToken;
import com.dev.moim.domain.account.repository.RefreshTokenRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

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
                        .expiration(validityMilliseconds/1000)
                        .build();

        refreshTokenRepository.save(newRefreshToken);
    }

    @Transactional
    public void deleteToken(String refreshToken) {
        refreshTokenRepository.deleteRefreshTokenByToken(refreshToken);
    }
}
