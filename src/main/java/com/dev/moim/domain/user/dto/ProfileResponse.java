package com.dev.moim.domain.user.dto;

public record ProfileResponse(
        Long userId,
        String username,
        String profileImageUrl
) {
}
