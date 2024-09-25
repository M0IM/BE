package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.enums.PostType;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateMoimPostDTO(
        Long moimId,
        @Length(min = 1, max = 255)
        String title,
        @Length(min = 1, max = 1500)
        String content,
        List<String> imageKeyNames,
        PostType postType
) {
}
