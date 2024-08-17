package com.dev.moim.domain.user.dto;

import java.time.LocalDateTime;

public record CreateIndividualPlanRequestDTO(
        LocalDateTime date,
        String content
) {
}
