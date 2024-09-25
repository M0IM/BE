package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.UserReview;
import org.springframework.data.domain.Page;

import java.util.List;

public record ReviewListDTO(
        List<ReviewDTO> reviewDTOList,
        Long totalReviewCnt,
        Boolean isFirst,
        Boolean hasNext
) {
    public static ReviewListDTO of(Page<UserReview> userReviewPage) {
        List<ReviewDTO> reviewDTOList = userReviewPage.stream()
                .map(ReviewDTO::of)
                .toList();

        return new ReviewListDTO(
                reviewDTOList,
                userReviewPage.getTotalElements(),
                userReviewPage.isFirst(),
                userReviewPage.hasNext()
                );
    }
}
