package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;

import java.time.LocalDateTime;
import java.util.List;

public record MoimPreviewDTO(
        Long moimId,
        String title,
        String description,
        MoimCategory category,
        String address,
        String profileImageUrl,
        Integer memberCount,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static MoimPreviewDTO toMoimPreviewDTO(Moim moim, String imageUrl) {
        return new MoimPreviewDTO(
                moim.getId(),
                moim.getName(),
                moim.getIntroduction(),
                moim.getMoimCategory(),
                moim.getLocation(),
                imageUrl,
                moim.getUserMoimList().size(),
                moim.getCreatedAt(),
                moim.getUpdatedAt()
        );
    }
}
