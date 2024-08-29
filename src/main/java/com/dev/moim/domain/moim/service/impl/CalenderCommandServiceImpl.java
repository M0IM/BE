package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.dto.calender.PlanCreateDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.Schedule;
import com.dev.moim.domain.moim.entity.UserPlan;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.CalenderCommandService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PlanException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.firebase.service.FcmService;
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
    private final AlarmService alarmService;
    private final FcmService fcmService;
    private final UserProfileRepository userProfileRepository;

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

        return planRepository.save(plan).getId();
    }

    @Override
    public Long joinPlan(User user, Long moimId, Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        UserPlan userPlan = UserPlan.builder()
                .user(user)
                .plan(plan)
                .build();

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        if (plan.getUser().getIsPushAlarm()) {
            alarmService.saveAlarm(user, plan.getUser(), "[" + plan.getMoim().getName() + "]" + plan.getTitle(), userProfile.getName() + " 님이 참여 신청했습니다", AlarmType.PUSH, AlarmDetailType.PLAN, plan.getMoim().getId(), null, null);
            fcmService.sendNotification(plan.getUser(), "[" + plan.getMoim().getName() + "]" + plan.getTitle(), userProfile.getName() + " 님이 참여 신청했습니다");
        }

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

        List<User> participantList = userPlanRepository.findByPlanId(planId).stream().map(UserPlan::getUser).toList();

        participantList.stream().filter(User::getIsPushAlarm).forEach(participant -> {
            alarmService.saveAlarm(plan.getUser(), participant, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 수정되었습니다. 변경사항을 확인해주세요.", AlarmType.PUSH, AlarmDetailType.PLAN, plan.getMoim().getId(), null, null);
            fcmService.sendNotification(participant, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 수정되었습니다. 변경사항을 확인해주세요.");
        });

        plan.updateSchedule(scheduleList);
    }

    @Override
    public void deletePlan(Long moimId, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        List<User> participantList = userPlanRepository.findByPlanId(planId).stream().map(UserPlan::getUser).toList();

        participantList.stream().filter(User::getIsPushAlarm).forEach(participant -> {
            alarmService.saveAlarm(plan.getUser(), participant, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 취소되었습니다.", AlarmType.PUSH, AlarmDetailType.PLAN, plan.getMoim().getId(), null, null);
            fcmService.sendNotification(participant, "[" + plan.getMoim().getName() + "]" + plan.getTitle(), "일정이 취소되었습니다.");
        });

        planRepository.delete(plan);
    }
}
