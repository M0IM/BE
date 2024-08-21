package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.*;

import java.util.List;

public interface CalenderQueryService {

    PlanMonthListDTO<List<UserPlanDTO>> getUserPlans(User user, int year, int month);

    PlanMonthListDTO<PlanDayListDTO> getMoimPlans(User user, Long moimId, int year, int month);

    PlanDetailDTO getPlanDetails(User user, Long moimId, Long planId);

    ScheduleListDTO getSchedules(Long moiId, Long planId);

    PlanParticipantListPageDTO getPlanParticipants(Long moimId, Long planId, int page, int size);

    boolean existsByUserIdAndPlanId(Long userId,Long planId);

    Long findPlanWriter(Long planId);
}
