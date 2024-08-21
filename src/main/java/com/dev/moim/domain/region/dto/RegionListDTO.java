package com.dev.moim.domain.region.dto;

import com.dev.moim.domain.region.entity.Dong;
import com.dev.moim.domain.region.entity.Sido;
import com.dev.moim.domain.region.entity.Sigungu;

import java.util.List;

public record RegionListDTO(
        List<RegionDTO> regionList
) {
    public static RegionListDTO toSido(List<Sido> sidoList) {
        List<RegionDTO> regionList = sidoList.stream()
                .map(sido -> new RegionDTO(sido.getId(), sido.getAddrName()))
                .toList();

        return new RegionListDTO(regionList);
    }

    public static RegionListDTO toSigungu(List<Sigungu> siguguList) {
        List<RegionDTO> regionList = siguguList.stream()
                .map(sigugu -> new RegionDTO(sigugu.getId(), sigugu.getAddrName()))
                .toList();

        return new RegionListDTO(regionList);
    }

    public static RegionListDTO toDong(List<Dong> dongList) {
        List<RegionDTO> regionList = dongList.stream()
                .map(dong -> new RegionDTO(dong.getId(), dong.getAddrName()))
                .toList();

        return new RegionListDTO(regionList);
    }
}
