package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.Plan;

import java.time.LocalDateTime;

public record MoimPlanDTO(
        Long planId,
        String title,
        String location,
        String locationDetail,
        LocalDateTime time,
        boolean isParticipant
) {
    public static MoimPlanDTO from(Plan plan, boolean isParticipant) {
        return new MoimPlanDTO(
                plan.getId(),
                plan.getTitle(),
                plan.getLocation(),
                plan.getLocationDetail(),
                plan.getDate(),
                isParticipant
        );
    }
}
