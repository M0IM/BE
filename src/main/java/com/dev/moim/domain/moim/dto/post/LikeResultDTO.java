package com.dev.moim.domain.moim.dto.post;

public record LikeResultDTO
        (
        Boolean isLike
) {
    public static LikeResultDTO toLikeResultDTO(Boolean isLike) {
        return new LikeResultDTO(isLike);
    }
}
