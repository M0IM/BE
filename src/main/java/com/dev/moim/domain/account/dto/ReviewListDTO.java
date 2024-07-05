package com.dev.moim.domain.account.dto;

import java.util.List;

public record ReviewListDTO(
        List<ReviewDTO> reviewDTOList
) {
}
