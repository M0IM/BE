package com.dev.moim.global.scheduler;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.repository.IndividualPlanRepository;
import com.dev.moim.domain.moim.repository.UserPlanRepository;
import com.dev.moim.domain.moim.repository.UserTodoRepository;
import com.dev.moim.global.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PlanScheduler {

    private final IndividualPlanRepository individualPlanRepository;
    private final UserPlanRepository userPlanRepository;
    private final UserTodoRepository userTodoRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;
    private final FcmService fcmService;

    @Scheduled(cron = "0 0 9 * * *")
    public void notifyDailyPlanCnt() {

        List<User> userList = userRepository.findAllByIsPushAlarmTrueAndDeviceIdNotNull();

        userList.forEach(user -> {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59, 999999000);;

            int individualPlanCnt = individualPlanRepository.countByUserAndDateBetween(user, startOfDay, endOfDay);
            int moimPlanCnt = userPlanRepository.countPlansByUserAndDateBetween(user, startOfDay, endOfDay);
            int todoPlanCnt = userTodoRepository.countByUserAndTodoDueDateBetween(user, startOfDay, endOfDay);

            String content = String.format(
                    "개인 일정: %d, 모임 일정: %d, 오늘 마감인 할 일: %d",
                    individualPlanCnt, moimPlanCnt, todoPlanCnt
            );

            alarmService.saveAlarm(user, user, "오늘 예정된 일정", content, AlarmType.PUSH, AlarmDetailType.PLAN, null, null, null);

            if (user.getIsPushAlarm() && user.getDeviceId() != null) {
                fcmService.sendPushNotification(user, "오늘 예정된 일정", content, AlarmDetailType.PLAN);
            }
        });
    }
}
