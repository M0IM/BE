package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.CreateMoimDTO;
import com.dev.moim.domain.moim.entity.Moim;

public interface MoimCommandService {
    Moim createMoim(User user, CreateMoimDTO createMoimDTO);
}
