package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;

public record MoimPostPreviewDTO(
        Long moimPostId,
        String title,
        String content,
        String writer,
        Integer commentCount,
        Integer likeCount,
        LocalDateTime createAt
) {
}
