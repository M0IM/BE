package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;

public record ChangeAuthorityRequestDTO(
        @CheckAdminValidation
        Long moimId,
        MoimRole moimRole,
        Long userId
) {
}
