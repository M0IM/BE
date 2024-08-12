package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IndividualPlanRepository extends JpaRepository<IndividualPlan, Long> {

    List<IndividualPlan> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
