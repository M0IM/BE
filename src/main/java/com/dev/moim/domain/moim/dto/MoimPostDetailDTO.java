package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MoimPostDetailDTO(
        Long moimPostId,
        String title,
        String content,
        String writer,
        Integer commentCount,
        Integer likeCount,
        List<MoimCommentDTO>moimCommentDTOList,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
}
