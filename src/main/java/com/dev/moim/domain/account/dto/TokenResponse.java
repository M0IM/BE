package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Provider;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        Provider provider
) {
}
