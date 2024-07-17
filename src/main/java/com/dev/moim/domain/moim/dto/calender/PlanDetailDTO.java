package com.dev.moim.domain.moim.dto.calender;

import java.time.LocalDateTime;
import java.util.List;

public record PlanDetailDTO(
        Long planId,
        String title,
        LocalDateTime date,
        String location,
        String cost,
        int participant,
        List<ScheduleCreateDTO> schedules,
        boolean isParticipant
) {
}
