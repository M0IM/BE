package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record MoimPostDetailDTO(
        Long moimPostId,
        String title,
        String content,
        String profileImage,
        String writer,
        Integer commentCount,
        Integer likeCount,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
    public static MoimPostDetailDTO toMoimPostDetailDTO(Post post) {
        return new MoimPostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getImageFileName(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getName(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                post.getUpdatedAt(),
                post.getCreatedAt()
        );
    }
}
