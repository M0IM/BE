package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.enums.MoimType;

public record JoinedMoimDTO(
        Long moimId,
        String moimImageUrl,
        String moimName,
        String introduction,
        MoimType moimType,
        String location,
        int participantCnt
) {
}
