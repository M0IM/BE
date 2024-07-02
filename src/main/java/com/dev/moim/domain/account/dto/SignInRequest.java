package com.dev.moim.domain.account.dto;

public record SignInRequest(
        String email,
        String password
) {
}
