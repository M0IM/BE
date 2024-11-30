package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.entity.enums.Provider;

public record ProfileDTO(
        Long userId,
        Long profileId,
        ProfileType profileType,
        String nickname,
        String imageUrl,
        Provider provider
) {
    public static ProfileDTO of(User user) {
        return new ProfileDTO(
                user.getId(),
                null,
                null,
                user.getNickname(),
                user.getImageUrl()!= null && ! user.getImageUrl().isEmpty() ? user.getImageUrl() : null,
                user.getProvider()
        );
    }
}
