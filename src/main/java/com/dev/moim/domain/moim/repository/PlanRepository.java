package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Plan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM UserPlan up JOIN up.plan p WHERE up.user = :user AND p.date BETWEEN :startOfDay AND :endOfDay ORDER BY p.date ASC")
    Slice<Plan> findByUserAndDateBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}
