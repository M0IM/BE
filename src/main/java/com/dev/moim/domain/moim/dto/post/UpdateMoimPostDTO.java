package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.global.validation.annotation.UserMoimValidaton;

import java.util.List;

public record UpdateMoimPostDTO(
        @UserMoimValidaton
        Long moimId,
        Long postId,
        String title,
        String content,
        List<String> imageKeyNames
) {
}
