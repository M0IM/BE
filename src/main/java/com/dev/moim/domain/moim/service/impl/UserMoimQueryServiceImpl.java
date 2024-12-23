package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.moim.dto.profile.UserMoimProfileDetailDTO;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.ProfileStatus;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.UserMoimQueryService;
import com.dev.moim.global.error.handler.MoimException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_MOIM_NOT_FOUND;

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

    @Override
    public Optional<UserMoim> findByUserIdAndMoimIdAndJoinStatusAndMoimRoleWithUserAndMoim(
            Long userId, Long moimId, JoinStatus joinStatus, MoimRole moimRole) {
        return userMoimRepository.findByUserIdAndMoimIdAndJoinStatusAndMoimRoleWithUserAndMoim(
                userId, moimId, joinStatus, moimRole);
    }

    @Override
    public UserMoimProfileDetailDTO getUserMoimProfile(Long moimId, Long userMoimId) {
        UserMoim userMoim = userMoimRepository.findByIdAndMoimId(userMoimId, moimId)
                .orElseThrow(() -> new MoimException(USER_MOIM_NOT_FOUND));

        int participateMoimCnt = userMoimRepository.countByUserIdAndJoinStatusAndProfileStatus(userMoim.getUser().getId(), JoinStatus.COMPLETE, ProfileStatus.PUBLIC);

        return UserMoimProfileDetailDTO.from(userMoim, participateMoimCnt);
    }
}
