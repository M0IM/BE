package com.dev.moim.domain.moim.service.impl.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import lombok.Getter;

@Getter
public class JoinRequestDTO {
    private Moim moim;
    private UserMoim userMoim;

    public JoinRequestDTO(Moim moim, UserMoim userMoim) {
        this.moim = moim;
        this.userMoim = userMoim;
    }
}
