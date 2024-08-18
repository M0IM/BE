package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanCreateDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.Schedule;
import com.dev.moim.domain.moim.entity.UserPlan;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.CalenderCommandService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PlanException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.dev.moim.global.common.code.status.ErrorStatus.MOIM_NOT_FOUND;
import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalenderCommandServiceImpl implements CalenderCommandService {

    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final MoimRepository moimRepository;

    @Override
    @Transactional
    public Long createPlan(User user, PlanCreateDTO request) {

        Moim moim = moimRepository.findById(request.moimId())
                .orElseThrow(() -> new MoimException(MOIM_NOT_FOUND));

        Plan plan = Plan.builder()
                .title(request.title())
                .date(request.date())
                .location(request.location())
                .locationDetail(request.locationDetail())
                .cost(request.cost())
                .scheduleList(new ArrayList<>())
                .moim(moim)
                .build();

        if (request.schedules() != null && !request.schedules().isEmpty()) {
            request.schedules().forEach(scheduleDTO -> {
                Schedule schedule = Schedule.builder()
                        .content(scheduleDTO.title())
                        .startTime(scheduleDTO.startTime())
                        .build();
                plan.addSchedule(schedule);
            });
        }

        Plan savedPlan = planRepository.save(plan);

        UserPlan userPlan = UserPlan.builder()
                .user(user)
                .plan(savedPlan)
                .isWriter(true)
                .build();

        userPlanRepository.save(userPlan);

        return savedPlan.getId();
    }

    @Override
    public Long joinPlan(User user, Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        UserPlan userPlan = UserPlan.builder()
                .isWriter(false)
                .user(user)
                .plan(plan)
                .build();

        return userPlanRepository.save(userPlan).getId();
    }
}
