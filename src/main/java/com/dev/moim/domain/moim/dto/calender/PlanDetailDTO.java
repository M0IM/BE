package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public record PlanDetailDTO(
        Long planId,
        String title,
        LocalDateTime date,
        String location,
        String cost,
        long participant,
        List<ScheduleCreateDTO> schedules,
        boolean isParticipant
) {

    public static PlanDetailDTO from(Plan plan, long participant, List<Schedule> scheduleList, Boolean isParticipant) {

        List<ScheduleCreateDTO> scheduleCreateDTOList = scheduleList.stream()
                .map(schedule -> new ScheduleCreateDTO(
                        schedule.getContent(),
                        schedule.getStartTime()
                )).toList();

        return new PlanDetailDTO(
                plan.getId(),
                plan.getTitle(),
                plan.getDate(),
                plan.getLocation(),
                plan.getCost(),
                participant,
                scheduleCreateDTOList,
                isParticipant
        );
    }
}
