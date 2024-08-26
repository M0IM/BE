package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO;

public record MoimJoinRequestDTO(
     Long userMoimId,
     Long moimId,
     String imageUrl,
     String title,
     String description,
     MoimCategory moimCategory,
     String location,
     Integer userCounts,
     JoinStatus joinStatus
) {
    public static MoimJoinRequestDTO toMoimJoinRequestDTO(JoinRequestDTO joinRequestDTO, Integer userCounts) {
        Moim moim = joinRequestDTO.getMoim();
        return new MoimJoinRequestDTO(joinRequestDTO.getUserMoim().getId(), moim.getId(), moim.getImageUrl(),moim.getName(), moim.getIntroduction(), moim.getMoimCategory(), moim.getLocation(), userCounts, joinRequestDTO.getUserMoim().getJoinStatus());
    }
}
