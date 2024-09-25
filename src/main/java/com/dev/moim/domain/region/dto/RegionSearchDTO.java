package com.dev.moim.domain.region.dto;

import com.dev.moim.domain.region.entity.Dong;

public record RegionSearchDTO(
        Long dongId,
        String regionTotalName
) {
    public static RegionSearchDTO of(Dong dong) {
        String sidoName = dong.getParent().getParent().getAddrName();
        String sigunguName = dong.getParent().getAddrName();
        return new RegionSearchDTO(
                dong.getId(),
                sidoName + " " + sigunguName + " " + dong.getAddrName()
        );
    }
}