package com.dev.moim.domain.user.dto;

import java.util.List;

public record UserPreviewListDTO(
        List<UserPreviewDTO> userPreviewDTOList,
        Boolean hasNext,
        Long nextCursor
) {

    public static UserPreviewListDTO toUserPreviewListDTO(List<UserPreviewDTO> userPreviewDTOList, Boolean hasNext, Long nextCursor) {
        return new UserPreviewListDTO(userPreviewDTOList, hasNext, nextCursor);
    }
}
