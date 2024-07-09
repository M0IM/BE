package com.dev.moim.domain.user.dto;

import java.util.List;

public record UserPreviewListDTO(
        List<UserPreviewDTO> userPreviewDTOList
) {
}
