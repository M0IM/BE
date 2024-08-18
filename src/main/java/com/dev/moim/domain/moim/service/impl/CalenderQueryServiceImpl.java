package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.dto.calender.*;
import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.entity.Schedule;
import com.dev.moim.domain.moim.entity.UserPlan;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.global.error.handler.MoimException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CalenderQueryServiceImpl implements CalenderQueryService {

    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserMoimRepository userMoimRepository;
    private final IndividualPlanRepository individualPlanRepository;

    @Override
    public PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<IndividualPlan> individualPlanList = individualPlanRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        Map<Integer, List<UserPlanDTO>> monthPlanListByDay = individualPlanList.stream()
                .map(UserPlanDTO::of)
                .collect(Collectors.groupingBy(dto -> dto.time().getDayOfMonth()));

        return new PlanMonthListDTO<>(monthPlanListByDay);
    }

    @Override
    public PlanMonthListDTO<List<UserPlanDTO>> getUserPlans(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<UserPlan> userPlanList = userPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate);

        Map<Integer, List<UserPlanDTO>> planListByDay = userPlanList.stream()
                .map(userPlan -> UserPlanDTO.of(userPlan.getPlan()))
                .collect(Collectors.groupingBy(dto -> dto.time().getDayOfMonth()));

        return new PlanMonthListDTO<>(planListByDay);
    }

    @Override
    public PlanMonthListDTO<PlanDayListDTO> getMoimPlans(User user, Long moimId, int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Plan> monthPlanList = planRepository.findByDateBetween(startDate, endDate);

        Map<Integer, List<Plan>> monthPlanListByDay = monthPlanList.stream()
                .collect(Collectors.groupingBy(plan -> plan.getDate().getDayOfMonth()));

        Map<Integer, PlanDayListDTO> planDayListMap = new HashMap<>();
        for (Map.Entry<Integer, List<Plan>> entry : monthPlanListByDay.entrySet()) {
            int day = entry.getKey();
            List<Plan> dayPlans = entry.getValue();

            LocalDateTime dayStart = YearMonth.of(year, month).atDay(day).atStartOfDay();
            LocalDateTime dayEnd = dayStart.plusDays(1).minusNanos(1);
            log.info("비교 시작 일 : {}", dayStart);
            log.info("비교 종료 일 : {}", dayEnd);
            int memberWithPlanCnt = userPlanRepository.countUsersWithPlansInDateRange(moimId, dayStart, dayEnd);

            List<MoimPlanDTO> planList = dayPlans.stream()
                    .filter(plan -> plan.getMoim().getId().equals(moimId))
                    .map(plan -> MoimPlanDTO.from(plan, userPlanRepository.existsByPlanIdAndUserId(plan.getId(), user.getId())))
                    .collect(Collectors.toList());

            planDayListMap.put(day, new PlanDayListDTO(memberWithPlanCnt, planList));
        }

        return new PlanMonthListDTO<>(planDayListMap);
    }

    @Override
    public PlanDetailDTO getPlanDetails(User user, Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new MoimException(PLAN_NOT_FOUND));

        long participant = userPlanRepository.countByPlanId(planId);

        List<Schedule> scheduleList = scheduleRepository.findAllByPlanId(planId);

        Boolean isParticipant = userPlanRepository.existsByUserIdAndPlanId(user.getId(), planId);

        return PlanDetailDTO.from(plan, participant, scheduleList, isParticipant);
    }

    @Override
    public ScheduleListDTO getSchedules(Long planId) {
        List<Schedule> scheduleList = scheduleRepository.findAllByPlanId(planId);

        return ScheduleListDTO.of(scheduleList);
    }

    @Override
    public PlanParticipantListPageDTO getPlanParticipants(Long moimId, Long planId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(Sort.Direction.ASC, "id"));
        Slice<UserPlan> userPlanPage = userPlanRepository.findByPlanIdWithUserAndUserMoim(planId, moimId, pageRequest);

        List<UserProfile> userProfileList = userPlanPage.stream()
                .map(UserPlan::getUser)
                .map(user -> userMoimRepository.findByUserIdAndMoimId(user.getId(), moimId))
                .filter(Optional::isPresent)
                .map(optionalUserMoim -> optionalUserMoim.get().getUserProfile())
                .toList();

        return PlanParticipantListPageDTO.from(userProfileList, userPlanPage);
    }

    @Override
    public Boolean existsByUserIdAndPlanId(Long userId, Long planId) {

        return userPlanRepository.existsByUserIdAndPlanId(userId, planId);
    }
}
