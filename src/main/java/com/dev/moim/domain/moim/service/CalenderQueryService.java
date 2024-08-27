package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.*;

public interface CalenderQueryService {

    PlanMonthListDTO<PlanDayListDTO> getMoimPlans(User user, Long moimId, int year, int month);

    PlanDetailDTO getPlanDetails(User user, Long planId);

    ScheduleListDTO getSchedules(Long moiId, Long planId);

    PlanParticipantListPageDTO getPlanParticipants(Long moimId, Long planId, int page, int size);

    boolean existsByUserIdAndPlanId(Long userId,Long planId);

    Long findPlanWriter(Long planId);
}
