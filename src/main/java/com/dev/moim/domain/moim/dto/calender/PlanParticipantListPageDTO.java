package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.dto.profile.UserMoimProfileDTO;
import com.dev.moim.domain.moim.entity.UserPlan;
import org.springframework.data.domain.Slice;

import java.util.List;

public record PlanParticipantListPageDTO(
        Boolean isFirst,
        Boolean hasNext,
        List<UserMoimProfileDTO> planParticipantList
) {
    public static PlanParticipantListPageDTO from(Slice<UserPlan> userPlanSlice) {
        List<UserMoimProfileDTO> profileDTOList = userPlanSlice.stream()
                .map(userPlan -> UserMoimProfileDTO.of(userPlan.getUserMoim()))
                .toList();

        return new PlanParticipantListPageDTO(
                userPlanSlice.isFirst(),
                userPlanSlice.hasNext(),
                profileDTOList
        );
    }
}
