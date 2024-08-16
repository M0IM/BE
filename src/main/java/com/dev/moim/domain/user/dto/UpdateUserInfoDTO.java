package com.dev.moim.domain.user.dto;

import com.dev.moim.global.validation.annotation.UserMoimListValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateUserInfoDTO(
        @NotBlank String nickname,
        String imageKey,
        @NotBlank String residence,
        String introduction,
        @Schema(description = "프로필에 공개할 모임 ID 리스트")
        @UserMoimListValidation List<Long> publicMoimList
) {
}
