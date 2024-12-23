package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

public record UserMoimPreviewDTO(
        Long userId,
        Long userMoimId,
        String nickname,
        String imageUrl,
        MoimRole moimRole
) {
    public static UserMoimPreviewDTO from(UserMoim userMoim) {
        return new UserMoimPreviewDTO(
                userMoim.getUser().getId(),
                userMoim.getId(),
                userMoim.getNickname(),
                userMoim.getImageUrl(),
                userMoim.getMoimRole()
        );
    }
}
