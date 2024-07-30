package com.dev.moim.domain.moim.dto.calender;

import java.time.LocalDateTime;
import java.util.List;

public record PlanCreateDTO(
        String title,
        LocalDateTime date,
        String locationAddress,
        String locationDetail,
        String cost,
        List<ScheduleCreateDTO> schedules
) {
}