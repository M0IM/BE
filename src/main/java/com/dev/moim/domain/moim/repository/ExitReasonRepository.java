package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.ExitReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExitReasonRepository extends JpaRepository<ExitReason, Long> {
}
