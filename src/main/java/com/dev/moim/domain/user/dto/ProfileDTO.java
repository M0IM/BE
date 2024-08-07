package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.UserProfile;

public record ProfileDTO(
        Long userId,
        String nickname,
        String imageUrl
) {
    public static ProfileDTO of(UserProfile userProfile) {
        return new ProfileDTO(
                userProfile.getUser().getId(),
                userProfile.getName(),
                userProfile.getImageFileName()
        );
    }
}
