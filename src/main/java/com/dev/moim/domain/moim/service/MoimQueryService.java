package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.MoimPreviewDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;

import java.util.List;

public interface MoimQueryService {

    MoimPreviewListDTO getMyMoim(User user);
}
