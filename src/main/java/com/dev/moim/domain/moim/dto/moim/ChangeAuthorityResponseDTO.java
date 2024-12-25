package com.dev.moim.domain.moim.dto.moim;

import com.dev.moim.domain.moim.entity.enums.MoimRole;

public record ChangeAuthorityResponseDTO(
        Long userId,
        MoimRole moimRole
) {
    public static ChangeAuthorityResponseDTO toChangeAuthorityResponseDTO(Long userId, MoimRole moimRole) {
        return new ChangeAuthorityResponseDTO(userId, moimRole);
    }
}
