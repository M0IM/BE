package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;

public record MoimPreviewDTO(
        Long moimId,
        String title,
        String description,
        String category,
        String address,
        String profileImageUrl,
        Integer memberCount,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
