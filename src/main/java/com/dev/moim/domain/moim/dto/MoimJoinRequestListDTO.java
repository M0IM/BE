package com.dev.moim.domain.moim.dto;

import java.util.List;

public record MoimJoinRequestListDTO(
        List<MoimJoinRequestDTO> moimJoinRequestDTOList,
        Boolean hasNext,
        Long nextCursor
) {
    public static MoimJoinRequestListDTO toMoimJoinRequestListDTO(List<MoimJoinRequestDTO> moimJoinRequestDTOList, Boolean hasNext, Long nextCursor) {
        return new MoimJoinRequestListDTO(moimJoinRequestDTOList, hasNext, nextCursor);
    }
}
