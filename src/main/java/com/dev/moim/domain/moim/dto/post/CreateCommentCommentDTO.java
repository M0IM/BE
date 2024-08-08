package com.dev.moim.domain.moim.dto.post;

public record CreateCommentCommentDTO(
        Long moimId,
        Long commentId,
        Long postId,
        String content
) {
}
