package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

import java.util.List;
import java.util.Optional;

public interface UserMoimQueryService {

    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusWithUserAndMoim(
            Long userId, Long moimId, JoinStatus joinStatus);

    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusInMoimRoleListWithUserAndMoim(
            Long userId, Long moimId, JoinStatus joinStatus, List<MoimRole> moimRoleList);

    Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusAndMoimRoleWithUserAndMoim(
            Long userId, Long moimId, JoinStatus joinStatus, MoimRole moimRole);
}
