package com.dev.moim.domain.moim.dto.calender;

public record PlanDTO(
        Long planId,
        String title,
        String location,
        String cost,
        int participantCnt,
        boolean isParticipant
) {
}
