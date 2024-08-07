package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long commentId,
        String content,
        Integer likeCount,
        String profileImage,
        String writer,
        Boolean isLike,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
    public static CommentResponseDTO toCommentResponseDTO(Comment comment, Boolean isLike) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCommentLikeList().size(),
                comment.getUserMoim() == null ? null : comment.getUserMoim().getUserProfile().getImageFileName(),
                comment.getUserMoim() == null ? null : comment.getUserMoim().getUserProfile().getName(),
                isLike,
                comment.getUpdatedAt(),
                comment.getCreatedAt()
        );
    }
}
