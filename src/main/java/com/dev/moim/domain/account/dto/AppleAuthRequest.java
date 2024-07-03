package com.dev.moim.domain.account.dto;

public record AppleAuthRequest(
        String identityToken,
        String appId,
        String nickname
) {
}
