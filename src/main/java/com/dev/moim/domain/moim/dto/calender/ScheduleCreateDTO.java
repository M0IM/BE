package com.dev.moim.domain.moim.dto.calender;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ScheduleCreateDTO(
        String title,
        @Schema(type = "string", example = "12:00:00")
        LocalTime startTime
) {
}
