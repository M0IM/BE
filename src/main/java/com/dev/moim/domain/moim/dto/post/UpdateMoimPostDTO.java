package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.global.validation.annotation.UserMoimValidaton;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateMoimPostDTO(
        @UserMoimValidaton
        Long moimId,
        Long postId,
        @Length(min = 1, max = 255)
        String title,
        @Length(min = 1, max = 2000)
        String content,
        List<String> imageKeyNames
) {
}
