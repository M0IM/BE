package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;

import java.time.LocalDateTime;

public record CreateMoimResultDTO(
        Long moimId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static CreateMoimResultDTO toCreateMoimResultDTO(Moim moim) {
        return new CreateMoimResultDTO(moim.getId(), moim.getCreatedAt(), moim.getUpdatedAt());
    }
}
