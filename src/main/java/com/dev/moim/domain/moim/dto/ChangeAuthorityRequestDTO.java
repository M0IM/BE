package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.MoimRole;
import jakarta.persistence.criteria.JoinType;

public record ChangeAuthorityRequestDTO(
        Long moimId,
        MoimRole moimRole,
        Long userId
) {
}
