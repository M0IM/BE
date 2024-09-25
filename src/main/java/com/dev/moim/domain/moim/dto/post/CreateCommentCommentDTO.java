package com.dev.moim.domain.moim.dto.post;

import org.hibernate.validator.constraints.Length;

public record CreateCommentCommentDTO(
        Long moimId,
        Long commentId,
        Long postId,
        @Length(min = 1, max = 255)
        String content
) {
}
