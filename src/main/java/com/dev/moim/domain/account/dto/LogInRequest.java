package com.dev.moim.domain.account.dto;

public record LogInRequest(
        String email,
        String password
) {
}
