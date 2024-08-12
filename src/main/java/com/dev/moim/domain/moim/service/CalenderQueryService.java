package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanDetailDTO;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.dto.calender.PlanParticipantListPageDTO;
import com.dev.moim.domain.moim.dto.calender.ScheduleListDTO;

public interface CalenderQueryService {

    PlanMonthListDTO getIndividualPlans(User user, int year, int month);

    PlanMonthListDTO getMoimPlans(User user, Long moimId, int year, int month);

    PlanDetailDTO getPlanDetails(User user, Long planId);

    ScheduleListDTO getSchedules(Long planId);

    PlanParticipantListPageDTO getPlanParticipants(Long moimId, Long planId, int page, int size);
}
