package com.dev.moim.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ProfileListDTO(
        List<ProfileDTO> profileDTOList,
        @Schema(description = "유저의 대표 프로필 Id")
        Long mainProfileId
) {
}
