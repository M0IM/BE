package com.dev.moim.domain.account.dto;

public record TokenResponse(
        String accessToken,
        // Long accessTokenExpiresIn,
        String refreshToken
) {
}
