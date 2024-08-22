package com.dev.moim.domain.moim.entity.enums;

public enum PlanType {
    MOIM_PLAN, INDIVIDUAL_PLAN;

    public static PlanType fromString(String type) {
        try {
            return PlanType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
