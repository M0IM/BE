package com.dev.moim.domain.moim.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CalenderDetailGetResponse(
        String title,
        LocalDateTime date,
        String location,
        String cost,
        int participant,
        List<CalenderSchedule> schedules
) {
}
