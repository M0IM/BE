package com.dev.moim.domain.moim.dto;

import java.util.List;

public record CreateMoimPostDTO(
        String title,
        String content,
        List<String>imageUrl
) {
}
