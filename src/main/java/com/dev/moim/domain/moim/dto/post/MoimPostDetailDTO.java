package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostImage;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.PostType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record MoimPostDetailDTO(
        Long moimPostId,
        String title,
        String content,
        Long writerId,
        String profileImage,
        String writer,
        Integer commentCount,
        Integer likeCount,
        Boolean isLike,
        PostType postType,
        List<String> imageKeyNames,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
    public static MoimPostDetailDTO toMoimPostDetailDTO(Post post, Boolean postLike, Optional<UserMoim> userMoim) {

        List<String> imageKeyNames = post.getPostImageList().stream().map(PostImage::getImageKeyName).toList();

        return new MoimPostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                userMoim.isEmpty() ? null : userMoim.get().getUserProfile().getId(),
                userMoim.isEmpty() ? null : userMoim.get().getUserProfile().getImageUrl(),
                userMoim.isEmpty() ? null : userMoim.get().getUserProfile().getName(),
                post.getCommentList().size(),
                post.getPostLikeList().size(),
                postLike,
                post.getPostType(),
                imageKeyNames,
                post.getUpdatedAt(),
                post.getCreatedAt()
        );
    }
}
