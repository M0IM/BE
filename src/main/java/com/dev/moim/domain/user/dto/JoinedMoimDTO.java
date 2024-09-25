package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;

public record JoinedMoimDTO(
        Long moimId,
        String moimImageUrl,
        String moimName,
        String introduction,
        MoimCategory moimCategory,
        String location,
        int participantCnt
) {
}
