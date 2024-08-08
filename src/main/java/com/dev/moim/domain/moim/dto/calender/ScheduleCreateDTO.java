package com.dev.moim.domain.moim.dto.calender;

import java.time.LocalDateTime;

public record ScheduleCreateDTO(
        String title,
        LocalDateTime startTime
) {
}
