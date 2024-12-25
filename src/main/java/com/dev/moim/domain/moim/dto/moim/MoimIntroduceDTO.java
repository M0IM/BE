package com.dev.moim.domain.moim.dto.moim;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;

public record MoimIntroduceDTO(
        String videoKeyName,
        String title,
        String writer,
        String writerProfileImage
) {
    public static MoimIntroduceDTO toMoimIntroduceDTO(Moim moim, UserMoim userMoim) {
        return new MoimIntroduceDTO(
                moim.getIntroduceVideoKeyName(),
                moim.getIntroduceVideoTitle(),
                userMoim.getNickname(),
                userMoim.getImageUrl());
    }
}
