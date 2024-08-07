package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByPlanId(Long planId);
}
