package com.dev.moim.domain.moim.dto;

import com.dev.moim.global.validation.annotation.UserMoimValidaton;

public record WithMoimDTO(
        @UserMoimValidaton
        Long moimId,
        String exitReason
) {
}
