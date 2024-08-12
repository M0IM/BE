package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;

import java.time.LocalDateTime;

public record UserPlanDTO(
        Long planId,
        String title,
        String location,
        String locationDetail,
        LocalDateTime time
) {
    public static UserPlanDTO of(IndividualPlan individualPlan) {
        return new UserPlanDTO(
                individualPlan.getId(),
                individualPlan.getTitle(),
                null,
                null,
                individualPlan.getDate()
        );
    }

    public static UserPlanDTO of(Plan plan) {
        return new UserPlanDTO(
                plan.getId(),
                plan.getTitle(),
                plan.getLocation(),
                plan.getLocationDetail(),
                plan.getDate()
        );
    }
}
