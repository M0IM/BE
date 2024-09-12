package com.dev.moim.domain.user.dto;

import com.dev.moim.global.validation.annotation.UserMoimListValidation;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateMultiProfileDTO(
        @NotBlank String nickname,
        String imageKey,
        String introduction,
        @UserMoimListValidation List<Long> targetMoimIdList
) {
}
