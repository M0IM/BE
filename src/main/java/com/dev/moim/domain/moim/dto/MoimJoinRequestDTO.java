package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.service.impl.dto.JoinRequestDTO;

public record MoimJoinRequestDTO(
     Long moimId,
     String title,
     String description,
     MoimCategory moimCategory,
     String location,
     Integer userCounts,
     JoinStatus joinStatus
) {
    public static MoimJoinRequestDTO toMoimJoinRequestDTO(JoinRequestDTO joinRequestDTO, Integer userCounts) {
        Moim moim = joinRequestDTO.getMoim();
        return new MoimJoinRequestDTO(moim.getId(), moim.getName(), moim.getIntroduction(), moim.getMoimCategory(), moim.getLocation(), userCounts, joinRequestDTO.getUserMoim().getJoinStatus());
    }
}
