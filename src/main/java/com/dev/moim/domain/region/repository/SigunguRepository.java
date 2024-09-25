package com.dev.moim.domain.region.repository;

import com.dev.moim.domain.region.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
    List<Sigungu> findByParentId(Long parentId);
}
