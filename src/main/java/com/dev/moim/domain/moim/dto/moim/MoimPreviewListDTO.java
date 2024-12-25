package com.dev.moim.domain.moim.dto.moim;

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
