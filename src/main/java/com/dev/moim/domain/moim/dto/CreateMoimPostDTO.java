package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.PostType;

import java.util.List;

public record CreateMoimPostDTO(
        Long moimId,
        String title,
        String content,
        List<String> imageKeyNames,
        PostType postType
) {
}
