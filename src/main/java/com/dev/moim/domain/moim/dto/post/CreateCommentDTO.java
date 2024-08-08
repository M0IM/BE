package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.enums.PostType;

import java.util.List;

public record CreateCommentDTO(
        Long moimId,
        Long postId,
        String content
) {
}
