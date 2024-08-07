package com.dev.moim.domain.moim.dto;

import java.util.List;

public record MoimPostPreviewListDTO(
        List<MoimPostPreviewDTO> moimPreviewList,
        Long nextCursor,
        Boolean hasNext
) {
    public static MoimPostPreviewListDTO toMoimPostPreviewListDTO(List<MoimPostPreviewDTO> moimPreviewList, Long nextCursor, Boolean hasNext) {
        return new MoimPostPreviewListDTO(moimPreviewList, nextCursor, hasNext);
    }
}
