package com.dev.moim.domain.account.dto;

public record LoginRequest(
        String email,
        String password,
        String fcmToken
) {
}
