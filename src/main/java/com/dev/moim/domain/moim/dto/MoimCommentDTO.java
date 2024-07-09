package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;

public record MoimCommentDTO(
        Long moimCommentId,
        String title,
        String content,
        String writer,
        Integer likeCount,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
}
