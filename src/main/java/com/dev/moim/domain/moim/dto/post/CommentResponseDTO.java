package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponseDTO(
        Long commentId,
        String content,
        Integer likeCount,
        String profileImage,
        String writer,
        Boolean isLike,
        LocalDateTime updateAt,
        LocalDateTime createAt,
        List<CommentCommentResponseDTO> commentResponseDTOList
) {
    public static CommentResponseDTO toCommentResponseDTO(Comment comment, Boolean isLike, List<CommentCommentResponseDTO> commentResponseDTOList) {
        UserMoim userMoim = comment.getUserMoim();

        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCommentLikeList().size(),
                userMoim == null ? null : comment.getUserMoim().getUserProfile().getImageUrl(),
                userMoim == null ? null : comment.getUserMoim().getUserProfile().getName(),
                isLike,
                comment.getUpdatedAt(),
                comment.getCreatedAt(),
                commentResponseDTOList
        );
    }
}
