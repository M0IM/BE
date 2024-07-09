package com.dev.moim.domain.account.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
