package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.enums.CommentStatus;

import java.time.LocalDateTime;

public record CommentCommentResponseDTO(
        Long commentId,
        String content,
        Integer likeCount,
        String profileImage,
        String writer,
        Boolean isLike,
        CommentStatus commentStatus,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
    public static CommentCommentResponseDTO toCommentCommentResponseDTO(Comment comment, Boolean isLike) {
        return new CommentCommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCommentLikeList().size(),
                comment.getUserMoim() == null ? null : comment.getUserMoim().getUserProfile().getImageUrl(),
                comment.getUserMoim() == null ? null : comment.getUserMoim().getUserProfile().getName(),
                isLike,
                comment.getCommentStatus(),
                comment.getUpdatedAt(),
                comment.getCreatedAt()
        );
    }
}
