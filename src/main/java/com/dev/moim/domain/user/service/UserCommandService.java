package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.UpdateUserInfoDTO;

public interface UserCommandService {
    void updateInfo(User user, UpdateUserInfoDTO request);
}
