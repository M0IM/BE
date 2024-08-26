package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostImage;
import com.dev.moim.domain.moim.entity.enums.PostType;

import java.time.LocalDateTime;
import java.util.List;

public record MoimPostPreviewDTO(
        Long moimPostId,
        Long moimId,
        String title,
        String content,
        List<String> moimImageUrl,
        String ownerProfileImageUrl,
        String writer,
        Integer commentCount,
        Integer likeCount,
        PostType postType,
        LocalDateTime createAt
) {
    public static MoimPostPreviewDTO toMoimPostPreviewDTO(Post post) {
        return new MoimPostPreviewDTO(
                post.getId(),
                post.getMoim().getId(),
                post.getTitle(),
                post.getContent(),
                post.getPostImageList() == null ? null : post.getPostImageList()
                        .stream().map(PostImage::getImageKeyName).toList(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getImageUrl(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getName(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                post.getPostType(),
                post.getCreatedAt()
        );
    }
}
