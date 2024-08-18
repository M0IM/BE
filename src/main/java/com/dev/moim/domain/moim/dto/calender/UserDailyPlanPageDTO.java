package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;
import org.springframework.data.domain.Slice;

import java.util.List;

public record UserDailyPlanPageDTO(
        Boolean isFirst,
        Boolean hasNext,
        List<UserPlanDTO> userPlanDTOList
) {
    public static UserDailyPlanPageDTO toUserMoimPlan(Slice<Plan> userMoimPlanSlice) {
        List<UserPlanDTO> userMoimPlanDTOList = userMoimPlanSlice.stream()
                .map(UserPlanDTO::of)
                .toList();

        return new UserDailyPlanPageDTO(
                userMoimPlanSlice.isFirst(),
                userMoimPlanSlice.hasNext(),
                userMoimPlanDTOList
        );
    }

    public static UserDailyPlanPageDTO toUserIndividualPlan(Slice<IndividualPlan> individualPlanSlice) {
        List<UserPlanDTO> userIndividualPlanDTOList = individualPlanSlice.stream()
                .map(UserPlanDTO::of)
                .toList();

        return new UserDailyPlanPageDTO(
                individualPlanSlice.isFirst(),
                individualPlanSlice.hasNext(),
                userIndividualPlanDTOList
        );
    }
}