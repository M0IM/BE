package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.UserPlan;
import com.dev.moim.domain.user.dto.ProfileDTO;
import org.springframework.data.domain.Slice;

import java.util.List;

public record PlanParticipantListPageDTO(
        Boolean isFirst,
        Boolean hasNext,
        List<ProfileDTO> planParticipantList
) {
    public static PlanParticipantListPageDTO from(List<UserProfile> userProfileList, Slice<UserPlan> userPlanSlice) {
        List<ProfileDTO> profileDTOList = userProfileList.stream()
                .map(ProfileDTO::of)
                .toList();

        return new PlanParticipantListPageDTO(
                userPlanSlice.isFirst(),
                userPlanSlice.hasNext(),
                profileDTOList
        );
    }
}
