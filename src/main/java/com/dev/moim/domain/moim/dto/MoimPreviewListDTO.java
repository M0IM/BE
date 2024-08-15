package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.dto.post.MoimPostPreviewDTO;
import com.dev.moim.domain.moim.dto.post.MoimPostPreviewListDTO;

import java.util.List;

public record MoimPreviewListDTO(
        List<MoimPreviewDTO> moimPreviewList,
        Long nextCursor,
        Boolean hasNext
) {
    public static MoimPreviewListDTO toMoimPreviewListDTO(List<MoimPreviewDTO> moimPreviewList, Long nextCursor, Boolean hasNext) {
        return new MoimPreviewListDTO(moimPreviewList, nextCursor, hasNext);
    }
}
