package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.moim.dto.profile.UpdateUserMoimProfileDTO;
import com.dev.moim.domain.moim.entity.UserMoim;

public interface UserMoimCommandService {

    Long updateUserMoimProfile(UserMoim userMoim, UpdateUserMoimProfileDTO dto);
}
