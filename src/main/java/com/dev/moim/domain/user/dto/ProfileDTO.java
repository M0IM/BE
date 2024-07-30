package com.dev.moim.domain.user.dto;

public record ProfileDTO(
        Long profileId,
        String nickname,
        String profileImageUrl
) {
}
