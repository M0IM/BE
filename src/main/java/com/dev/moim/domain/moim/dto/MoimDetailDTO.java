package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.user.dto.UserPreviewDTO;

import java.time.LocalDateTime;
import java.util.List;

public record MoimDetailDTO(
        Long moimId,
        JoinStatus joinStatus,
        String title,
        String description,
        String profileImageUrl,
        MoimCategory category,
        Double averageAge,
        int diaryCount,
        int moimReviewCount,
        Long maleCount,
        Long femaleCount,
        int memberCount,
        String address,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        List<UserPreviewDTO> userPreviewDTOList
) {
    public static MoimDetailDTO toMoimDetailDTO(Moim moim, JoinStatus joinStatus, String profileImageUrl, Double averageAge, int diaryCount, int moimReviewCount, Long maleCount, Long femaleCount, int memberCount, List<UserPreviewDTO> userPreviewDTOList) {
        return new MoimDetailDTO(
                moim.getId(),
                joinStatus,
                moim.getName(),
                moim.getIntroduction(),
                profileImageUrl,
                moim.getMoimCategory(),
                averageAge,
                diaryCount,
                moimReviewCount,
                maleCount,
                femaleCount,
                memberCount,
                moim.getLocation(),
                moim.getCreatedAt(),
                moim.getUpdatedAt(),
                userPreviewDTOList
        );
    }
}
