package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public record PlanDetailDTO(
        Long planId,
        Long writerId,
        String title,
        LocalDateTime date,
        String location,
        String locationDetail,
        String cost,
        long participant,
        List<ScheduleGetDTO> schedules,
        boolean isParticipant
) {

    public static PlanDetailDTO from(Plan plan, long participant, List<Schedule> scheduleList, Boolean isParticipant) {

        List<ScheduleGetDTO> scheduleGetDTOList = scheduleList.stream()
                .map(schedule -> new ScheduleGetDTO(
                        schedule.getId(),
                        schedule.getStartTime(),
                        schedule.getContent()
                )).toList();

        return new PlanDetailDTO(
                plan.getId(),
                plan.getUser().getId(),
                plan.getTitle(),
                plan.getDate(),
                plan.getLocation(),
                plan.getLocationDetail(),
                plan.getCost(),
                participant,
                scheduleGetDTOList,
                isParticipant
        );
    }
}
