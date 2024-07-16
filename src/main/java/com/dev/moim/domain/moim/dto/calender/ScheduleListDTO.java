package com.dev.moim.domain.moim.dto.calender;

import java.util.List;

public record ScheduleListDTO(
        List<ScheduleGetDTO> scheduleList
) {
}
