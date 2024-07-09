package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;

public record CreateMoimResultDTO(
        Long moimId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
