package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MoimDetailDTO(
        Long moimId,
        String title,
        String description,
        List<String> category,
        Double averageAge,
        Long diaryCount,
        Long moimReviewCount,
        Long maleCount,
        Long femaleCount,
        Long memberCount,
        Long field,
        String address,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
