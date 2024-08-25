package com.dev.moim.domain.region.dto;

import com.dev.moim.domain.region.entity.Dong;
import org.springframework.data.domain.Slice;

import java.util.List;

public record RegionSearchListDTO(
        List<RegionSearchDTO> regionSearchDTOList,
        Long nextCursor,
        Boolean hasNext
) {
    public static RegionSearchListDTO of(Slice<Dong> dongSlice) {
        List<RegionSearchDTO> regionSearchDTOList = dongSlice.stream()
                .map(RegionSearchDTO::of)
                .toList();

        Long nextCursor = dongSlice.hasNext()
                ? dongSlice.getContent().get(dongSlice.getNumberOfElements() - 1).getId() + 1
                : null;

        return new RegionSearchListDTO(
                regionSearchDTOList,
                nextCursor,
                dongSlice.hasNext()
        );
    }
}