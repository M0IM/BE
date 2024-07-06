package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.User;

public record SignUpResponse(
        String email,
        String password
) {
    public static SignUpResponse of(User user) {
        return new SignUpResponse(
                user.getEmail(),
                user.getPassword()
        );
    }
}
