package com.dev.moim.global.scheduler;

import com.dev.moim.domain.moim.service.TodoCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TodoScheduler {

    private final TodoCommandService todoCommandService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateTodosToExpiredStatus() {
        todoCommandService.updateExpiredTodosAndAssigneesStatus();
    }
}
