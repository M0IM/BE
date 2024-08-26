package com.dev.moim.domain.user.dto;

import com.dev.moim.global.validation.annotation.SelfReviewValidation;

public record CreateReviewDTO(
        @SelfReviewValidation Long targetUserId,
        Double rating,
        String content
) {
}
