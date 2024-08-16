package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.UserReview;

public record CreateReviewResultDTO(
        Long reviewId
) {
    public static CreateReviewResultDTO of(UserReview userReview) {
        return new CreateReviewResultDTO(
                userReview.getId()
        );
    }
}
