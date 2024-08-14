package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;

public interface MoimQueryService {

    MoimPreviewListDTO getMyMoim(User user);
}
