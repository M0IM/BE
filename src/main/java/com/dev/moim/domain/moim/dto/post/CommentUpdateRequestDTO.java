package com.dev.moim.domain.moim.dto.post;

public record CommentUpdateRequestDTO(
        Long commentId,
        String content
) {
}
