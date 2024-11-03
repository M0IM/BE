package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;

import java.util.Optional;

public interface UserMoimQueryService {

    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusWithUserAndMoim(
            Long userId, Long moimId, JoinStatus joinStatus);
}
