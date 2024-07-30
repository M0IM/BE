package com.dev.moim.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ProfileCreateDTO(
        String profileImageUrl,
        String nickname,
        String residence,
        String introduction,
        @Schema(description = "유저가 참여하는 모임 중, 유저가 선택한 '프로필에 표시할 모임의 id'를 넣어주세요.")
        List<Long> displayMoimIdList
) {
}
