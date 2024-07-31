package com.dev.moim.domain.user;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository user;
    private final UserRepository userRepository;

    public ProfileDTO getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorStatus._BAD_REQUEST));

        return ProfileDTO.of(user);
    }
}
