package com.dev.moim.domain.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProfileDetailGetResponse(
        String profileImageUrl,
        String username,
        boolean isMainProfile,
        String residence,
        LocalDate birth,
        LocalDateTime createdAt,
        double rating,
        int joinedMoimCount,
        List<receivedReview> receivedReviewList,
        String introduction
) {
}
