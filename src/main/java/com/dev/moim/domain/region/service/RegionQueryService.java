package com.dev.moim.domain.region.service;

import com.dev.moim.domain.region.dto.RegionListDTO;
import com.dev.moim.domain.region.dto.RegionSearchListDTO;

public interface RegionQueryService {

    RegionListDTO getSido();

    RegionListDTO getSigungu(Long parentId);

    RegionListDTO getDong(Long parentId);

    RegionSearchListDTO getRegions(String searchTerm, Long cursor, Integer take);
}
