package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostImage;

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
        List<String> imageKeyNames,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
    public static MoimPostDetailDTO toMoimPostDetailDTO(Post post) {

        List<String> imageKeyNames = post.getPostImageList().stream().map(PostImage::getImageKeyName).toList();

        return new MoimPostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getImageFileName(),
                post.getUserMoim() == null ? null : post.getUserMoim().getUserProfile().getName(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                imageKeyNames,
                post.getUpdatedAt(),
                post.getCreatedAt()
        );
    }
}
