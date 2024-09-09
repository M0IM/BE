package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.enums.PlanType;
import com.dev.moim.global.util.TimeUtil;
import org.springframework.data.domain.Slice;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public record UserDailyPlanPageDTO(
        Boolean isFirst,
        Boolean hasNext,
        List<UserPlanDTO> userPlanDTOList
) {
    public static UserDailyPlanPageDTO toUserMoimPlan(Slice<Plan> userMoimPlanSlice, List<UserPlanDTO> userPlanDTOList) {
        return new UserDailyPlanPageDTO(
                userMoimPlanSlice.isFirst(),
                userMoimPlanSlice.hasNext(),
                userPlanDTOList
        );
    }

    public static UserDailyPlanPageDTO toUserIndividualPlan(Slice<IndividualPlan> individualPlanSlice) {
        List<UserPlanDTO> userIndividualPlanDTOList = individualPlanSlice.stream()
                .map(UserPlanDTO::toIndividualPlan)
                .toList();

        return new UserDailyPlanPageDTO(
                individualPlanSlice.isFirst(),
                individualPlanSlice.hasNext(),
                userIndividualPlanDTOList
        );
    }

    public static UserDailyPlanPageDTO toUserDailyPlan(List<Object[]> userDailyPlanList, boolean isFirst, boolean hasNext) {
        List<UserPlanDTO> userDailyPlanDTOList = userDailyPlanList.stream().map(record -> {
            Long planId = (Long) record[0];
            String title = (String) record[1];
            Timestamp timestamp = (Timestamp) record[2];
            LocalDateTime time = TimeUtil.toLocalDateTime(timestamp);
            String location = (String) record[3];
            String locationDetail = (String) record[4];
            String memo = (String) record[5];
            Long moimId = (Long) record[6];
            String moimName = (String) record[7];
            String planTypeString = (String) record[8];
            PlanType planType = PlanType.fromString(planTypeString);

            return UserPlanDTO.toDailyUserPlan(
                    planId,
                    title,
                    time,
                    location,
                    locationDetail,
                    memo,
                    moimId,
                    moimName,
                    planType
            );
        }).toList();

        return new UserDailyPlanPageDTO(
                isFirst,
                hasNext,
                userDailyPlanDTOList
        );
    }
}