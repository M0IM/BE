package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.enums.CommentStatus;

public record BlockCommentResponse(
        Long commentId,
        String content,
        CommentStatus commentStatus
) {
    public static BlockCommentResponse toBlockCommentResponse(Comment comment) {
        return new BlockCommentResponse(comment.getId(), comment.getContent(), comment.getCommentStatus());
    }
}
