package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
