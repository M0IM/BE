package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.global.validation.annotation.UserMoimListValidation;

import java.util.List;

public record CreateProfileDTO(
        String nickname,
        String residence,
        String introduction,
        String imageKey,
        @UserMoimListValidation List<Long> targetMoimIdList,
        ProfileType profileType
) {
}
