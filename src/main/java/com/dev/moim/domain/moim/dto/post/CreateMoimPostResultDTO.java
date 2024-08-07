package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;

import java.time.LocalDateTime;

public record CreateMoimPostResultDTO(
        Long moimPostId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static CreateMoimPostResultDTO toCreateMoimPostDTO(Post post) {
        return new CreateMoimPostResultDTO(post.getId(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
