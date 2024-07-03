package com.dev.moim.domain.account.dto;

public record ReissueTokenResponse(
        String accessToken,
        String refreshToken
) {
}
