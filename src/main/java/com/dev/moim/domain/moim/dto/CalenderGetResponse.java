package com.dev.moim.domain.moim.dto;

import java.util.List;

public record CalenderGetResponse(
        String title,
        String location,
        String cost,
        int participant,
        List<ParticipantProfile> participantProfileList,
        boolean hasDetailedSchedule
) {
}
