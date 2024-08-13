package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.service.UserService;
import com.dev.moim.global.error.handler.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moim.domain.account.entity.enums.ProfileType.MAIN;
import static com.dev.moim.global.common.code.status.ErrorStatus.USER_PROFILE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public ProfileDTO getProfile(User user) {
        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return ProfileDTO.of(user, userProfile);
    }

    @Override
    public ProfileDetailDTO getDetailProfile(User user) {
        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return ProfileDetailDTO.from(user, userProfile);
    }
}
