package com.dev.moim.domain.moim.dto.calender;

import com.dev.moim.domain.moim.entity.Plan;
import org.springframework.data.domain.Slice;

import java.util.List;

public record UserDailyPlanPageDTO(
        Boolean isFirst,
        Boolean hasNext,
        List<UserPlanDTO> userPlanDTOList
) {
    public static UserDailyPlanPageDTO of(Slice<Plan> userMoimPlanSlice) {
        List<UserPlanDTO> userMoimPlanDTOList = userMoimPlanSlice.stream()
                .map(UserPlanDTO::of)
                .toList();

        return new UserDailyPlanPageDTO(
                userMoimPlanSlice.isFirst(),
                userMoimPlanSlice.hasNext(),
                userMoimPlanDTOList
        );
    }
}