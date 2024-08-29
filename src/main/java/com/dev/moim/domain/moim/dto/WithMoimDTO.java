package com.dev.moim.domain.moim.dto;

import com.dev.moim.global.validation.annotation.UserMoimValidaton;
import org.hibernate.validator.constraints.Length;

public record WithMoimDTO(
        @UserMoimValidaton
        Long moimId,
        @Length(min = 1, max = 255)
        String exitReason
) {
}
