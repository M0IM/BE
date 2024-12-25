package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.PostType;

import java.time.LocalDateTime;

public record MoimPostPreviewDTO(
        Long moimPostId,
        Long moimId,
        String title,
        String content,
        String moimImageUrl,
        String ownerProfileImageUrl,
        String writer,
        Long writerId,
        Integer commentCount,
        Integer likeCount,
        PostType postType,
        LocalDateTime createAt
) {
    public static MoimPostPreviewDTO toMoimPostPreviewDTO(Post post) {
        UserMoim userMoim = post.getUserMoim();

        return new MoimPostPreviewDTO(
                post.getId(),
                post.getMoim().getId(),
                post.getTitle(),
                post.getContent(),
                post.getMoim().getImageUrl(),
                userMoim == null ? null : userMoim.getImageUrl(),
                userMoim == null  ? null : userMoim.getNickname(),
                userMoim == null  ? null : userMoim.getUser().getId(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                post.getPostType(),
                post.getCreatedAt()
        );
    }
}
