package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Comment;

import java.time.LocalDateTime;

public record CreateCommentResultDTO(
        Long moimPostId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static CreateCommentResultDTO toCreateCommentResultDTO(Comment comment) {
        return new CreateCommentResultDTO(comment.getId(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
