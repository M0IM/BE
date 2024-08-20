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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CalenderCommandServiceImpl implements CalenderCommandService {

    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final MoimRepository moimRepository;

    @Override
    public Long createPlan(User user, Long moimId, PlanCreateDTO request) {

        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new MoimException(MOIM_NOT_FOUND));

        Plan plan = Plan.builder()
                .title(request.title())
                .date(LocalDateTime.of(request.date(), request.startTime()))
                .location(request.location())
                .locationDetail(request.locationDetail())
                .cost(request.cost())
                .scheduleList(new ArrayList<>())
                .user(user)
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
                .build();

        userPlanRepository.save(userPlan);

        return savedPlan.getId();
    }

    @Override
    public Long joinPlan(User user, Long moimId, Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        UserPlan userPlan = UserPlan.builder()
                .user(user)
                .plan(plan)
                .build();

        return userPlanRepository.save(userPlan).getId();
    }

    @Override
    public void cancelPlanParticipation(User user, Long moidId, Long planId) {

        UserPlan userPlan = userPlanRepository.findByUserIdAndPlanId(user.getId(), planId)
                .orElseThrow(() -> new PlanException(USER_NOT_PART_OF_PLAN));

        userPlanRepository.delete(userPlan);
    }

    @Override
    public void updatePlan(Long moimId, Long planId, PlanCreateDTO request) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        List<Schedule> scheduleList = request.schedules().stream()
                .map(schedule -> Schedule.builder()
                        .content(schedule.title())
                        .startTime(schedule.startTime())
                        .plan(plan)
                        .build())
                .toList();

        plan.updatePlan(
                request.title(),
                LocalDateTime.of(request.date(), request.startTime()),
                request.location(),
                request.locationDetail(),
                request.cost()
        );

        plan.updateSchedule(scheduleList);
    }

    @Override
    public void deletePlan(Long moimId, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        planRepository.delete(plan);
    }
}
