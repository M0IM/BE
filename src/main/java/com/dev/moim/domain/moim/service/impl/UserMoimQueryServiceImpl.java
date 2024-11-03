package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.UserMoimQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusInMoimRoleListWithUserAndMoim(
            Long userId, Long moimId, JoinStatus joinStatus, List<MoimRole> moimRoleList) {
        return userMoimRepository.findByUserIdAndMoimIdAndJoinStatusInMoimRoleListWithUserAndMoim(
                userId, moimId, joinStatus, moimRoleList);
    }
}
