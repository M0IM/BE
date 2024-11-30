package com.dev.moim.domain.moim.dto.profile;

public record UpdateUserMoimProfileDTO(
        String nickname,
        String imageUrl,
        String introduction
) {
}
