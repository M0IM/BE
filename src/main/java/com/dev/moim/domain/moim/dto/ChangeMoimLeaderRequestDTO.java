package com.dev.moim.domain.moim.dto;

import com.dev.moim.global.validation.annotation.CheckOwnerValidation;

public record ChangeMoimLeaderRequestDTO(
        Long userId,
        @CheckOwnerValidation
        Long moimId
) {
}
