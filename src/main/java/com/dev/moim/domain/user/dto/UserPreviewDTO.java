package com.dev.moim.domain.user.dto;

public record UserPreviewDTO(
        Long userId,
        String nickname,
        String imageUrl,
        String userRank
) {
}
