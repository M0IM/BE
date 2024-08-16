package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.UserReview;

public record ReviewDTO(
        Long reviewId,
        String content,
        Double rating
) {
    public static ReviewDTO of(UserReview userReview) {
        return new ReviewDTO(
                userReview.getId(),
                userReview.getContent(),
                userReview.getRating()
        );
    }
}
