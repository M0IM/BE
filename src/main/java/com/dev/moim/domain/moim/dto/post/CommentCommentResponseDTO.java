package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record CommentCommentResponseDTO(
        Long commentId,
        String content,
        Integer likeCount,
        String profileImage,
        String writer,
        Long writerId,
        Boolean isLike,
        CommentStatus commentStatus,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
    public static CommentCommentResponseDTO toCommentCommentResponseDTO(Comment comment, Boolean isLike, List<Comment> blockComments, Optional<UserMoim> userMoim) {
        boolean b1 = blockComments.stream().anyMatch((b) -> {
                    if (comment.equals(b)) {
                        return true;
                    }
                    return false;
                }
        );

        return new CommentCommentResponseDTO(
                comment.getId(),
                b1 ? null : comment.getContent(),
                comment.getCommentLikeList().size(),
                userMoim.isEmpty() || b1 ? null : userMoim.get().getUserProfile().getImageUrl(),
                userMoim.isEmpty() || b1 ? null : userMoim.get().getUserProfile().getName(),
                userMoim.isEmpty() || b1 ? null : userMoim.get().getUser().getId(),
                isLike,
                b1 ? CommentStatus.BLOCKED : comment.getCommentStatus(),
                comment.getUpdatedAt(),
                comment.getCreatedAt()
        );
    }
}
