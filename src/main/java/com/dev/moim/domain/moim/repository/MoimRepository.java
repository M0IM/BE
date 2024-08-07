package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Moim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimRepository extends JpaRepository<Moim, Long> {
}
