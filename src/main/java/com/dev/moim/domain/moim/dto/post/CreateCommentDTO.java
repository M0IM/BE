package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.enums.PostType;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateCommentDTO(
        Long moimId,
        Long postId,
        @Length(min = 1, max = 255)
        String content
) {
}
