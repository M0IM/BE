package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;

public record ProfileDTO(
        Long userId,
        String email,
        String nickname,
        Provider provider
) {
    public static ProfileDTO of(User user) {
        return new ProfileDTO(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProvider()
        );
    }
}
