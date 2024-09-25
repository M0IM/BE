package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

public record UserPreviewDTO(
        Long userId,
        String nickname,
        String imageKeyName,
        MoimRole moimRole
) {
    public static UserPreviewDTO toUserPreviewDTO (UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileDTO.getUserProfile();
        return new UserPreviewDTO(userProfile.getUser().getId(), userProfile.getName(), userProfile.getImageUrl(), userProfileDTO.getUserMoim().getMoimRole());
    }
}
