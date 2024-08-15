package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.CreateMoimDTO;
import com.dev.moim.domain.moim.dto.UpdateMoimDTO;
import com.dev.moim.domain.moim.dto.WithMoimDTO;
import com.dev.moim.domain.moim.entity.Moim;
import jakarta.validation.Valid;

public interface MoimCommandService {
    Moim createMoim(User user, CreateMoimDTO createMoimDTO);

    void withDrawMoim(User user, @Valid WithMoimDTO withMoimDTO);

    void modifyMoimInfo(@Valid UpdateMoimDTO updateMoimDTO);
}
