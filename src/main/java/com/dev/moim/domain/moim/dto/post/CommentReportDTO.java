package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.global.validation.annotation.UserMoimValidaton;

public record CommentReportDTO(
        @UserMoimValidaton
        Long moimId,
        Long postId,
        Long commentId
) {
}