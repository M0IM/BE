package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.MoimRequestType;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;

public interface MoimQueryService {

    MoimPreviewListDTO getMyMoim(User user, Long cursor, Integer take);

    MoimPreviewListDTO findMoims(MoimRequestType moimRequestType, String name, Long cursor, Integer take);
}