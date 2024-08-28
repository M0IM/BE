package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.PostImage;
import com.dev.moim.domain.moim.entity.enums.PostType;

import java.util.List;

public record UpdatePostResponseDTO(
        Long postId,
        String title,
        String content,
        PostType postType,
        List<String> images
) {
    public static UpdatePostResponseDTO toUpdatePostResponseDTO(Post post) {
        List<String> images = post.getPostImageList().stream().map((i) -> i.getImageKeyName()).toList();

        return new UpdatePostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getPostType(), images);
    }
}
