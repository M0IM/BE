package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.*;

import java.util.List;

public interface CalenderQueryService {

    PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month);

    PlanMonthListDTO<List<UserPlanDTO>> getUserPlans(User user, int year, int month);

    PlanMonthListDTO<PlanDayListDTO> getMoimPlans(User user, Long moimId, int year, int month);

    PlanDetailDTO getPlanDetails(User user, Long planId);

    ScheduleListDTO getSchedules(Long planId);

    PlanParticipantListPageDTO getPlanParticipants(Long moimId, Long planId, int page, int size);

    Boolean existsByUserIdAndPlanId(Long userId,Long planId);
}
