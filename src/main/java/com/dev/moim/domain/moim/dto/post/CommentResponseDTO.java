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
    public static CommentResponseDTO toCommentResponseDTO(Comment comment, Boolean isLike, List<CommentCommentResponseDTO> commentResponseDTOList, List<Comment> blockComments) {
        UserMoim userMoim = comment.getUserMoim();

        boolean b1 = blockComments.stream().anyMatch((b) -> {
                    if (comment.equals(b)) {
                        return true;
                    }
                    return false;
            }
        );

        return new CommentResponseDTO(
                comment.getId(),
                b1 ?  null : comment.getContent(),
                comment.getCommentLikeList().size(),
                userMoim == null || b1 ? null : comment.getUserMoim().getUserProfile().getImageUrl(),
                userMoim == null || b1 ? null : comment.getUserMoim().getUserProfile().getName(),
                isLike,
                comment.getUpdatedAt(),
                comment.getCreatedAt(),
                commentResponseDTOList
        );
    }
}
