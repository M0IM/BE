package com.dev.moim.domain.moim.dto.profile;

import com.dev.moim.domain.moim.entity.UserMoim;

public record UserMoimProfileDTO(
        Long userMoimId,
        String nickname,
        String imageUrl
) {
    public static UserMoimProfileDTO of(UserMoim userMoim) {
        return new UserMoimProfileDTO(
                userMoim.getId(),
                userMoim.getNickname(),
                userMoim.getImageUrl()!= null && !userMoim.getImageUrl().isEmpty() ? userMoim.getImageUrl() : null
        );
    }
}
