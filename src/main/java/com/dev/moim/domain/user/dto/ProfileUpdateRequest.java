package com.dev.moim.domain.user.dto;

import java.util.List;

public record ProfileUpdateRequest(
        String profileImageUrl,
        String username,
        String residence,
        String introduction,
        List<Long> displayMoimList
) {
}
