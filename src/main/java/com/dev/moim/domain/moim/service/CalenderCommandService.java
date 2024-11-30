package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.moim.dto.calender.PlanCreateDTO;
import com.dev.moim.domain.moim.entity.UserMoim;

public interface CalenderCommandService {

    Long createPlan(UserMoim userMoim, PlanCreateDTO request);

    Long joinPlan(UserMoim userMoim, Long planId);

    void cancelPlanParticipation(UserMoim userMoim, Long planId);

    void updatePlan(UserMoim userMoim, Long planId, PlanCreateDTO request);

    void deletePlan(UserMoim userMoim, Long planId);
}
