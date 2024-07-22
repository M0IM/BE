package com.dev.moim.domain.account.dto;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
