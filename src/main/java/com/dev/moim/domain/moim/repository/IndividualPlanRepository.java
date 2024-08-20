package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.IndividualPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IndividualPlanRepository extends JpaRepository<IndividualPlan, Long> {

    List<IndividualPlan> findByUserIdAndDateBetween(Long userID, LocalDateTime startDate, LocalDateTime endDate);

    Slice<IndividualPlan> findByUserAndDateBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

    int countByUserAndDateBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
