package com.dev.moim.domain.moim.dto;

import com.dev.moim.global.validation.annotation.CheckAdminValidation;

public record MoimJoinConfirmRequestDTO(
        @CheckAdminValidation
        Long moimId,
        Long userId
) {
}
