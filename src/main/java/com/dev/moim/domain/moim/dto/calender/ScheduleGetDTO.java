package com.dev.moim.domain.moim.dto.calender;

import java.time.LocalTime;

public record ScheduleGetDTO(
        Long scheduleId,
        LocalTime startTime,
        String content
) {
}
