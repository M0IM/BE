package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.dto.profile.UserMoimProfileDTO;
import com.dev.moim.domain.moim.entity.UserPlan;
import org.springframework.data.domain.Page;

import java.util.List;

public record PlanParticipantListPageDTO(
        Boolean isFirst,
        Boolean hasNext,
        List<UserMoimProfileDTO> planParticipantList
) {
    public static PlanParticipantListPageDTO from(Page<UserPlan> userPlanPage) {
        List<UserMoimProfileDTO> profileDTOList = userPlanPage.stream()
                .map(userPlan -> UserMoimProfileDTO.of(userPlan.getUserMoim()))
                .toList();

        return new PlanParticipantListPageDTO(
                userPlanPage.isFirst(),
                userPlanPage.hasNext(),
                profileDTOList
        );
    }
}
