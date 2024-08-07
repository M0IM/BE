package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.global.validation.annotation.UserMoimValidaton;

import java.time.LocalDateTime;
import java.util.List;

public record PlanCreateDTO(
        Long userId,
        @UserMoimValidaton Long moimId,
        String title,
        LocalDateTime date,
        String location,
        String locationDetail,
        String cost,
        List<ScheduleCreateDTO> schedules
) {
}