package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanCreateDTO;
import com.dev.moim.domain.moim.dto.calender.UpdatePlanDTO;

public interface CalenderCommandService {

    Long createPlan(User user, Long moimId, PlanCreateDTO request);

    Long joinPlan(User user, Long moimId, Long planId);

    void cancelPlanParticipation(User user, Long moidId, Long planId);

    void updatePlan(Long moimId, Long planId, UpdatePlanDTO request);
}
