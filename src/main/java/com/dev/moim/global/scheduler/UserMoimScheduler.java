package com.dev.moim.global.scheduler;

import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserMoimScheduler {

    private final UserMoimRepository userMoimRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateTodosToExpiredStatus() {
        log.info("confirm userMoim delete 시작");

        userMoimRepository.deleteAllByConfirmUserMoim(List.of(JoinStatus.LOADING, JoinStatus.COMPLETE));

        log.info("confirm userMoim delete 종료");
    }

}
