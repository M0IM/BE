package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;

import java.time.LocalDateTime;
import java.util.List;

public record MoimDetailDTO(
        Long moimId,
        String title,
        String description,
        MoimCategory category,
        Double averageAge,
        int diaryCount,
        int moimReviewCount,
        Long maleCount,
        Long femaleCount,
        int memberCount,
        String address,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static MoimDetailDTO toMoimDetailDTO(Moim moim, Double averageAge, int diaryCount, int moimReviewCount, Long maleCount, Long femaleCount, int memberCount) {
        return new MoimDetailDTO(
                moim.getId(),
                moim.getName(),
                moim.getIntroduction(),
                moim.getMoimCategory(),
                averageAge,
                diaryCount,
                moimReviewCount,
                maleCount,
                femaleCount,
                memberCount,
                moim.getLocation(),
                moim.getCreatedAt(),
                moim.getUpdatedAt()
        );
    }
}
