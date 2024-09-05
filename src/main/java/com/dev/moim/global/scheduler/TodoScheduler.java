package com.dev.moim.global.scheduler;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.domain.moim.repository.TodoRepository;
import com.dev.moim.domain.moim.repository.UserTodoRepository;
import com.dev.moim.domain.moim.service.TodoCommandService;
import com.dev.moim.global.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TodoScheduler {

    private final TodoCommandService todoCommandService;
    private final TodoRepository todoRepository;
    private final UserTodoRepository userTodoRepository;
    private final AlarmService alarmService;
    private final FcmService fcmService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateTodosToExpiredStatus() {
        todoCommandService.updateExpiredTodosAndAssigneesStatus();
    }

    @Scheduled(cron = "0 0 16 * * *")
    public void notifyDueTodos() {

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfTomorrow = tomorrow.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Todo> dueTomorrowTodoList = todoRepository.findAllByDueDateBetween(tomorrow, endOfTomorrow);

        dueTomorrowTodoList.forEach(todo -> {
            List<UserTodo> userTodoList = userTodoRepository.findAllByTodoIdAndStatusNot(todo.getId(), TodoAssigneeStatus.COMPLETE);

            userTodoList.forEach(userTodo -> {
                User assignee = userTodo.getUser();

                alarmService.saveAlarm(todo.getWriter(), assignee, "마감 기한이 하루 남았습니다.", todo.getTitle(), AlarmType.PUSH, AlarmDetailType.TODO, todo.getMoim().getId(), null, null);

                if (assignee.getIsPushAlarm() && assignee.getDeviceId() != null) {
                    fcmService.sendNotification(assignee, "마감 기한이 하루 남았습니다.", todo.getTitle());
                }
            });
        });
    }
}
