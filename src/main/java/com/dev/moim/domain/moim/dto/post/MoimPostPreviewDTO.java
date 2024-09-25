package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostImage;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.PostType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public static MoimPostPreviewDTO toMoimPostPreviewDTO(Post post, Optional<UserMoim> userMoim) {
        return new MoimPostPreviewDTO(
                post.getId(),
                post.getMoim().getId(),
                post.getTitle(),
                post.getContent(),
                post.getMoim().getImageUrl(),
                userMoim.isEmpty() ? null : userMoim.get().getUserProfile().getImageUrl(),
                userMoim.isEmpty() ? null : userMoim.get().getUserProfile().getName(),
                userMoim.isEmpty() ? null : userMoim.get().getUser().getId(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                post.getPostType(),
                post.getCreatedAt()
        );
    }
}
