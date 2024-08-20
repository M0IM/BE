package com.dev.moim.domain.region.service.impl;

import com.dev.moim.domain.region.dto.RegionListDTO;
import com.dev.moim.domain.region.repository.DongRepository;
import com.dev.moim.domain.region.repository.SidoRepository;
import com.dev.moim.domain.region.repository.SigunguRepository;
import com.dev.moim.domain.region.service.RegionQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionQueryServiceImpl implements RegionQueryService {

    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;
    private final DongRepository dongRepository;

    @Override
    public RegionListDTO getSido() {
        return RegionListDTO.toSido(sidoRepository.findAll());
    }

    @Override
    public RegionListDTO getSigungu(Long parentId) {
        return RegionListDTO.toSigungu(sigunguRepository.findByParentId(parentId));
    }

    @Override
    public RegionListDTO getDong(Long parentId) {
        return RegionListDTO.toDong(dongRepository.findByParentId(parentId));
    }
}
