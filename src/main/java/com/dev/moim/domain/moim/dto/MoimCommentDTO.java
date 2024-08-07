package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Post;

import java.time.LocalDateTime;

public record MoimCommentDTO(
        Long moimCommentId,
        String content,
        String writer,
        Integer likeCount,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
}
