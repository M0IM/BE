package com.dev.moim.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateIndividualPlanRequestDTO(
        String title,
        LocalDate date,
        @Schema(type = "string", example = "12:00:00")
        LocalTime startTime,
        String location,
        String locationDetail,
        String memo
) {
}
