package com.dev.moim.domain.user.dto;

public record CreateReviewDTO(
        Long targetUserId,
        Double rating,
        String content
) {
}
