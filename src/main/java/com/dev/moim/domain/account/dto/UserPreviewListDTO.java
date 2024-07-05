package com.dev.moim.domain.account.dto;

import java.util.List;

public record UserPreviewListDTO(
        List<UserPreviewDTO> userPreviewDTOList
) {
}
