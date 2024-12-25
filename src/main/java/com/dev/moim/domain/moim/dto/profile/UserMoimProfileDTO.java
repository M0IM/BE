package com.dev.moim.domain.moim.dto.profile;

import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.moim.entity.UserMoim;

// TODO : userProfile 제거 후, profileId, profileType 필드 제거 (provider 도 확인)
public record UserMoimProfileDTO(
        Long userId,
        Long userMoimId,
        Long profileId,
        ProfileType profileType,
        String nickname,
        String imageUrl,
        Provider provider
) {
    public static UserMoimProfileDTO of(UserMoim userMoim) {
        return new UserMoimProfileDTO(
                userMoim.getUser().getId(),
                userMoim.getId(),
                null,
                null,
                userMoim.getNickname(),
                userMoim.getImageUrl()!= null && ! userMoim.getImageUrl().isEmpty() ? userMoim.getImageUrl() : null,
                userMoim.getUser().getProvider()
        );
    }
}
