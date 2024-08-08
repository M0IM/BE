package com.dev.moim.domain.moim.dto.calender;

import java.time.LocalDateTime;

public record ScheduleGetDTO(
        Long scheduleId,
        LocalDateTime startTime,
        String content
) {
}
