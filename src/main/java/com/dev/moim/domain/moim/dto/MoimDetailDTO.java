package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;

import java.time.LocalDateTime;
import java.util.List;

public record MoimDetailDTO(
        Long moimId,
        Boolean isJoin,
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
        List<String> userImages
) {
    public static MoimDetailDTO toMoimDetailDTO(Moim moim, Boolean isJoin, String profileImageUrl, Double averageAge, int diaryCount, int moimReviewCount, Long maleCount, Long femaleCount, int memberCount, List<String> userImages) {
        return new MoimDetailDTO(
                moim.getId(),
                isJoin,
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
                userImages
        );
    }
}
