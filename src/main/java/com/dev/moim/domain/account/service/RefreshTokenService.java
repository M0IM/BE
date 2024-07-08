package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.dto.ReissueTokenResponse;
import com.dev.moim.domain.account.entity.RefreshToken;
import com.dev.moim.domain.account.repository.RefreshTokenRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.principal.PrincipalDetails;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
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
    private final PrincipalDetailsService principalDetailsService;

    public boolean validateRefreshToken(String refreshToken) {
        RefreshToken savedrefreshToken =
                refreshTokenRepository
                        .findByEmail(jwtUtil.getEmail(refreshToken))
                        .orElseThrow(() -> new AuthException(NOT_CONTAIN_TOKEN));

        return savedrefreshToken.getToken().equals(refreshToken);
    }

    public ReissueTokenResponse reissueToken(String refreshToken) {
        try {
            validateRefreshToken(refreshToken);

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(jwtUtil.getEmail(refreshToken));

            return new ReissueTokenResponse(
                    jwtUtil.createAccessToken(principalDetails),
                    jwtUtil.createRefreshToken(principalDetails)
            );
        } catch (IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException eje) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }

    @Transactional
    public void saveToken(String refreshToken) {
        RefreshToken newRefreshToken =
                RefreshToken.builder()
                        .email(jwtUtil.getEmail(refreshToken))
                        .token(refreshToken)
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
