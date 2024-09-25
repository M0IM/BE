package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.PlanType;

import java.time.LocalDateTime;

import static com.dev.moim.domain.moim.entity.enums.PlanType.*;

public record UserPlanDTO(
        Long planId,
        String title,
        LocalDateTime time,
        String location,
        String locationDetail,
        String memo,
        Long moimId,
        String moimName,
        PlanType planType,
        MoimRole moimRole
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
                null,
                INDIVIDUAL_PLAN,
                null
        );
    }

    public static UserPlanDTO toUserMoimPlan(Plan plan, UserMoim userMoim) {
        return new UserPlanDTO(
                plan.getId(),
                plan.getTitle(),
                plan.getDate(),
                plan.getLocation(),
                plan.getLocationDetail(),
                null,
                plan.getMoim().getId(),
                plan.getMoim().getName(),
                MOIM_PLAN,
                userMoim.getMoimRole()
        );
    }

    public static UserPlanDTO toUserMoimTodo(Todo todo) {
        return new UserPlanDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDueDate(),
                null,
                null,
                null,
                todo.getMoim().getId(),
                todo.getMoim().getName(),
                TODO_PLAN,
                null
        );
    }

    public static UserPlanDTO toDailyUserPlan(Long planId,
                                              String title,
                                              LocalDateTime time,
                                              String location,
                                              String locationDetail,
                                              String memo,
                                              Long moimId,
                                              String moimName,
                                              PlanType planType) {
        return new UserPlanDTO(
                planId,
                title,
                time,
                location,
                locationDetail,
                memo,
                moimId,
                moimName,
                planType,
                null
        );
    }
}
