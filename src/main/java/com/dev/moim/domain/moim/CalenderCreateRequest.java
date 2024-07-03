package com.dev.moim.domain.moim;

import java.time.LocalDateTime;
import java.util.List;

public record CalenderCreateRequest(
        String title,
        LocalDateTime date,
        String location,
        String cost,
        int participant,
        List<CalenderSchedule> schedules
) {
}