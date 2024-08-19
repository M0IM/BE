package com.dev.moim.domain.moim.dto.calender;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PlanCreateDTO(
        String title,
        LocalDate date,
        @Schema(type = "string", example = "12:00:00")
        LocalTime startTime,
        String location,
        String locationDetail,
        String cost,
        List<ScheduleCreateDTO> schedules
) {
}