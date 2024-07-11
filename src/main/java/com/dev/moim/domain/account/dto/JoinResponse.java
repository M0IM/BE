package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.User;

public record JoinResponse(
        String email,
        String password
) {
    public static JoinResponse of(User user) {
        return new JoinResponse(
                user.getEmail(),
                user.getPassword()
        );
    }
}
