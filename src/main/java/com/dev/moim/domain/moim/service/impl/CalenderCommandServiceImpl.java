package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.dto.calender.PlanCreateDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.CalenderCommandService;
import com.dev.moim.global.error.handler.PlanException;
import com.dev.moim.global.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CalenderCommandServiceImpl implements CalenderCommandService {

    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final AlarmService alarmService;
    private final FcmService fcmService;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Long createPlan(UserMoim userMoim, PlanCreateDTO request) {

        Plan plan = Plan.builder()
                .title(request.title())
                .date(LocalDateTime.of(request.date(), request.startTime()))
                .location(request.location())
                .locationDetail(request.locationDetail())
                .cost(request.cost())
                .scheduleList(new ArrayList<>())
                .userMoim(userMoim)
                .moim(userMoim.getMoim())
                .build();

        if (request.schedules() != null && !request.schedules().isEmpty()) {
            request.schedules().forEach(scheduleDTO -> {
                Schedule schedule = Schedule.builder()
                        .title(scheduleDTO.title())
                        .startTime(scheduleDTO.startTime())
                        .build();
                plan.addSchedule(schedule);
            });
        }

        return planRepository.save(plan).getId();
    }

    @Override
    public Long joinPlan(UserMoim userMoim, Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        UserPlan userPlan = UserPlan.builder()
                .user(userMoim.getUser())
                .userMoim(userMoim)
                .plan(plan)
                .build();

        Optional.of(plan.getUserMoim())
                .filter(writer -> !userPlan.getUserMoim().equals(writer))
                .ifPresent(writer -> {
                    alarmService.saveAlarm(userMoim.getUser(), writer.getUser(), "[" + plan.getMoim().getName() + "]" + plan.getTitle(), userMoim.getNickname()+ " 님이 참여 신청했습니다", AlarmType.PUSH, AlarmDetailType.PLAN, plan.getMoim().getId(), null, null);

                    if (writer.getUser().getIsPushAlarm() && writer.getUser().getDeviceId() != null) {
                        fcmService.sendPushNotification(writer.getUser(), "[" + plan.getMoim().getName() + "]" + plan.getTitle(), userMoim.getNickname() + " 님이 참여 신청했습니다", AlarmDetailType.PLAN);
                    }
                });

        return userPlanRepository.save(userPlan).getId();
    }

    @Override
    public void cancelPlanParticipation(UserMoim userMoim, Long planId) {

        UserPlan userPlan = userPlanRepository.findByUserIdAndPlanId(userMoim.getUser().getId(), planId)
                .orElseThrow(() -> new PlanException(USER_NOT_PART_OF_PLAN));

        userPlanRepository.delete(userPlan);
    }

    @Override
    public void updatePlan(UserMoim userMoim, Long planId, PlanCreateDTO request) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        List<Schedule> scheduleList = request.schedules().stream()
                .map(schedule -> Schedule.builder()
                        .title(schedule.title())
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

        List<User> participantList = userPlanRepository.findByPlanId(planId).stream().map(UserPlan::getUser).toList();

        participantList.stream()
                .filter(participantUser -> !userMoim.getUser().equals(participantUser))
                .forEach(participantUser -> {
            alarmService.saveAlarm(userMoim.getUser(), participantUser, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 수정되었습니다. 변경사항을 확인해주세요.", AlarmType.PUSH, AlarmDetailType.PLAN, plan.getMoim().getId(), null, null);

            if (participantUser.getIsPushAlarm() && participantUser.getDeviceId() != null) {
                fcmService.sendPushNotification(participantUser, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 수정되었습니다. 변경사항을 확인해주세요.", AlarmDetailType.PLAN);
            }

        });

        plan.updateSchedule(scheduleList);
    }

    @Override
    public void deletePlan(UserMoim userMoim, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        List<User> participantList = userPlanRepository.findByPlanId(planId).stream().map(UserPlan::getUser).toList();

        participantList.stream()
                .filter(participantUser -> !userMoim.getUser().equals(participantUser))
                .forEach(participantUser -> {
            alarmService.saveAlarm(userMoim.getUser(), participantUser, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 취소되었습니다.", AlarmType.PUSH, AlarmDetailType.PLAN, plan.getMoim().getId(), null, null);

            if (participantUser.getIsPushAlarm() && participantUser.getDeviceId() != null) {
                fcmService.sendPushNotification(participantUser, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 취소되었습니다.", AlarmDetailType.PLAN);
            }
        });

        planRepository.delete(plan);
    }
}
