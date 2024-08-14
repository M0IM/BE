package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;

public record ProfileDTO(
        Long userId,
        String nickname,
        String imageUrl
) {
    public static ProfileDTO of(User user, UserProfile userProfile) {
        return new ProfileDTO(
                user.getId(),
                userProfile.getName(),
                userProfile.getImageFileName()
        );
    }
}
