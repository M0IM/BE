package com.dev.moim.domain.user.dto;

import java.util.List;

public record ReviewListDTO(
        List<ReviewDTO> reviewDTOList
) {
}
