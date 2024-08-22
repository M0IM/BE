package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.enums.PlanType;

import java.time.LocalDateTime;

import static com.dev.moim.domain.moim.entity.enums.PlanType.INDIVIDUAL_PLAN;
import static com.dev.moim.domain.moim.entity.enums.PlanType.MOIM_PLAN;

public record UserPlanDTO(
        Long planId,
        String title,
        LocalDateTime time,
        String location,
        String locationDetail,
        String memo,
        String moimName,
        PlanType planType
) {
    public static UserPlanDTO toIndividualPlan(IndividualPlan individualPlan) {
        return new UserPlanDTO(
                individualPlan.getId(),
                individualPlan.getTitle(),
                individualPlan.getDate(),
                individualPlan.getLocation(),
                individualPlan.getLocationDetail(),
                individualPlan.getMemo(),
                null,
                INDIVIDUAL_PLAN
        );
    }

    public static UserPlanDTO toUserMoimPlan(Plan plan) {
        return new UserPlanDTO(
                plan.getId(),
                plan.getTitle(),
                plan.getDate(),
                plan.getLocation(),
                plan.getLocationDetail(),
                null,
                plan.getMoim().getName(),
                MOIM_PLAN
        );
    }

    public static UserPlanDTO toDailyUserPlan(Long planId,
                                              String title,
                                              LocalDateTime time,
                                              String location,
                                              String locationDetail,
                                              String memo,
                                              String moimName,
                                              PlanType planType) {
        return new UserPlanDTO(
                planId,
                title,
                time,
                location,
                locationDetail,
                memo,
                moimName,
                planType
        );
    }
}
