package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
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
    public static ProfileDTO of(User user, UserProfile userProfile) {
        return new ProfileDTO(
                user.getId(),
                userProfile.getId(),
                userProfile.getProfileType(),
                userProfile.getName(),
                userProfile.getImageUrl()!= null && !userProfile.getImageUrl().isEmpty() ? userProfile.getImageUrl() : null,
                user.getProvider()
        );
    }
}
