package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

public record UserPreviewDTO(
        Long userId,
        Long userMoimId,
        String nickname,
        String imageKeyName,
        MoimRole moimRole
) {
    public static UserPreviewDTO toUserPreviewDTO (UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileDTO.getUserProfile();
        return new UserPreviewDTO(
                userProfile.getUser().getId(),
                userProfileDTO.getUserMoim().getId(),
                userProfile.getName(),
                userProfile.getImageUrl(),
                userProfileDTO.getUserMoim().getMoimRole());
    }

    public static UserPreviewDTO from(UserMoim userMoim) {
        return new UserPreviewDTO(
                userMoim.getUser().getId(),
                userMoim.getId(),
                userMoim.getNickname(),
                userMoim.getImageUrl(),
                userMoim.getMoimRole());
    }
}
