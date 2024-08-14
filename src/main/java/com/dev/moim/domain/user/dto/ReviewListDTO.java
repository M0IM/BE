package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record ReviewListDTO(
        List<ReviewDTO> reviewDTOList,
        int totalReviewCnt
) {
    public static ReviewListDTO of(Page<UserReview> userReviewSlice, Pageable pageable) {
        List<ReviewDTO> reviewDTOList = userReviewSlice.stream()
                .map(ReviewDTO::of)
                .toList();

        return new ReviewListDTO(reviewDTOList, pageable.getPageSize());
    }
}
