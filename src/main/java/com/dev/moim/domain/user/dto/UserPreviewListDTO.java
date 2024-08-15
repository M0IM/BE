package com.dev.moim.domain.user.dto;

import java.util.List;

public record UserPreviewListDTO(
        List<UserPreviewDTO> userPreviewDTOList
) {

    public static UserPreviewListDTO toUserPreviewListDTO(List<UserPreviewDTO> userPreviewDTOList) {
        return new UserPreviewListDTO(userPreviewDTOList);
    }
}
