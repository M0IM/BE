package com.dev.moim.domain.moim.service.impl.dto;


import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.UserMoim;
import lombok.Getter;

@Getter
public class UserProfileDTO {
    private UserProfile userProfile;
    private UserMoim userMoim;

    public UserProfileDTO(UserProfile userProfile, UserMoim userMoim) {
        this.userProfile = userProfile;
        this.userMoim = userMoim;
    }
}
