package com.dev.moim.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateUserInfoDTO(
        @NotBlank String nickname,
        @NotBlank String residence,
        String introduction,
        @Schema(description = "프로필에 공개할 모임 ID 리스트")
        List<Long> publicMoimList
) {
}
