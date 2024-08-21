package com.dev.moim.domain.moim.dto;

import com.dev.moim.global.validation.annotation.CheckAdminValidation;

public record ChangeMoimLeaderRequestDTO(
        Long userId,
        @CheckAdminValidation
        Long moimId
) {
}
