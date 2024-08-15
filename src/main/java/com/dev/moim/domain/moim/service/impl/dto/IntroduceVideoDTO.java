package com.dev.moim.domain.moim.service.impl.dto;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.Moim;
import lombok.Getter;

@Getter
public class IntroduceVideoDTO {
    private Moim moim;
    private UserProfile userProfile;

    public IntroduceVideoDTO(Moim moim, UserProfile userProfile) {
        this.moim = moim;
        this.userProfile = userProfile;
    }
}
