package com.dev.moim.domain.moim.dto.profile;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

import java.time.LocalDateTime;

public record UserMoimProfileDetailDTO(
        Long userId,
        Long userMoimId,
        String nickname,
        String imageUrl,
        String introduction,
        MoimRole moimRole,
        LocalDateTime createdAt,
        double rating,
        int participateMoimCnt
) {
    public static UserMoimProfileDetailDTO from(UserMoim userMoim, int participateMoimCnt) {
        return new UserMoimProfileDetailDTO(
                userMoim.getUser().getId(),
                userMoim.getId(),
                userMoim.getNickname(),
                userMoim.getImageUrl(),
                userMoim.getIntroduction(),
                userMoim.getMoimRole(),
                userMoim.getCreatedAt(),
                userMoim.getUser().getRating(),
                participateMoimCnt
        );
    }
}
