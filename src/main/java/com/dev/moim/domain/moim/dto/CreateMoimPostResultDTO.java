package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;

public record CreateMoimPostResultDTO(
        Long moimPostId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
