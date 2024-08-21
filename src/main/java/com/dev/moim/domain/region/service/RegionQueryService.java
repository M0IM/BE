package com.dev.moim.domain.region.service;

import com.dev.moim.domain.region.dto.RegionListDTO;

public interface RegionQueryService {

    RegionListDTO getSido();

    RegionListDTO getSigungu(Long parentId);

    RegionListDTO getDong(Long parentId);
}
