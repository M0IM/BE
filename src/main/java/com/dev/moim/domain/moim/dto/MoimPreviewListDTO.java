package com.dev.moim.domain.moim.dto;

import java.util.List;

public record MoimPreviewListDTO(
        List<MoimPreviewDTO> moimPreviewList
) {
    public static MoimPreviewListDTO toMoimPreviewListDTO(List<MoimPreviewDTO> moimPreviewList) {
        return new MoimPreviewListDTO(moimPreviewList);
    }
}
