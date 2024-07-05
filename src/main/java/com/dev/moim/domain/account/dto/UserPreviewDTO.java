package com.dev.moim.domain.account.dto;

public record UserPreviewDTO(
        Long userId,
        String nickname,
        String imageUrl,
        String userRank
) {
}
