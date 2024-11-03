package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.UserMoimQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMoimQueryServiceImpl implements UserMoimQueryService {

    private final UserMoimRepository userMoimRepository;

    @Override
    public Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusWithUserAndMoim(Long userId, Long moimId, JoinStatus joinStatus) {
        return userMoimRepository.findByUserIdAndMoimIdAndJoinStatusWithUserAndMoim(
                userId, moimId, joinStatus);
    }
}
