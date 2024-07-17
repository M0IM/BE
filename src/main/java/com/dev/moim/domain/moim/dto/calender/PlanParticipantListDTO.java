package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.user.dto.ProfileDTO;

import java.util.List;

public record PlanParticipantListDTO(
        List<ProfileDTO> planParticipantList
) {
}
