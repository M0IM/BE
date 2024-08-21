package com.dev.moim.domain.region.repository;

import com.dev.moim.domain.region.entity.Dong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findByParentId(Long parentId);
}
