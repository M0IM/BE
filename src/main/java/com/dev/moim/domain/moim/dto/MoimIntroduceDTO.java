package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.moim.entity.Moim;

public record MoimIntroduceDTO(
        String videoKeyName,
        String title,
        String writer,
        String writerProfileImage
) {
    public static MoimIntroduceDTO toMoimIntroduceDTO(Moim moim, UserProfile userProfile) {
        return new MoimIntroduceDTO(moim.getIntroduceVideoKeyName(), moim.getIntroduceVideoTitle(), userProfile.getName(), userProfile.getImageUrl());
    }
}
