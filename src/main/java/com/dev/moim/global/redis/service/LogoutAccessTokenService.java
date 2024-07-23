package com.dev.moim.global.redis.service;

import com.dev.moim.global.redis.entity.LogoutAccessToken;
import com.dev.moim.global.redis.repository.LogoutAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LogoutAccessTokenService {

    private final LogoutAccessTokenRepository logoutAccessTokenRepository;

    @Transactional
    public void saveToken(String accessToken, Long ExpSeconds) {

        LogoutAccessToken logoutAccessToken = LogoutAccessToken.builder()
                .accessToken(accessToken)
                .expiration(ExpSeconds)
                .build();

        logoutAccessTokenRepository.save(logoutAccessToken);

        log.info("transactionName = {}", TransactionSynchronizationManager.getCurrentTransactionName());
    }

    public Boolean isTokenExist(String accessToken) {
        return logoutAccessTokenRepository
                .existsById(accessToken);
    }
}