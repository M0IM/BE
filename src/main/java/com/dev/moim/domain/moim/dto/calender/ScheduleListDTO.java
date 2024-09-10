package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.Schedule;

import java.util.List;

public record ScheduleListDTO(
        List<ScheduleGetDTO> scheduleList
) {
    public static ScheduleListDTO of(List<Schedule> scheduleList) {
        List<ScheduleGetDTO> scheduleGetDTOList = scheduleList.stream()
                .map(schedule -> new ScheduleGetDTO(
                        schedule.getId(),
                        schedule.getStartTime(),
                        schedule.getTitle()
                )).toList();

        return new ScheduleListDTO(
                scheduleGetDTOList
        );
    }
}
