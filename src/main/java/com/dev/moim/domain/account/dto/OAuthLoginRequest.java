package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Provider;

public record OAuthLoginRequest(
        Provider provider,
        String idToken,
        String email
) {
}