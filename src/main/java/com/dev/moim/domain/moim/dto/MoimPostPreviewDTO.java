package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.enums.PostType;

import java.time.LocalDateTime;

public record MoimPostPreviewDTO(
        Long moimPostId,
        String title,
        String content,
        String profileImage,
        String writer,
        Integer commentCount,
        Integer likeCount,
        PostType postType,
        LocalDateTime createAt
) {
    public static MoimPostPreviewDTO toMoimPostPreviewDTO(Post post) {
        return new MoimPostPreviewDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getImageFileName(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getName(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                post.getPostType(),
                post.getCreatedAt()
        );
    }
}
