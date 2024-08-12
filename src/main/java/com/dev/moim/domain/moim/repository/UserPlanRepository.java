package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.UserPlan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {

    Boolean existsByPlanIdAndUserId(Long planId, Long userId);

    @Query("SELECT COUNT(DISTINCT u.id) " +
            "FROM User u " +
            "LEFT JOIN UserPlan up ON u.id = up.user.id " +
            "LEFT JOIN Plan p ON up.plan.id = p.id " +
            "LEFT JOIN IndividualPlan ip ON u.id = ip.user.id " +
            "WHERE (p.date BETWEEN :startDate AND :endDate AND up.user.id IN " +
            "(SELECT um.user.id FROM UserMoim um WHERE um.moim.id = :moimId)) " +
            "OR (ip.date BETWEEN :startDate AND :endDate AND u.id IN " +
            "(SELECT um.user.id FROM UserMoim um WHERE um.moim.id = :moimId))")
    int countUsersWithPlansInDateRange(@Param("moimId") Long moimId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    long countByPlanId(Long planId);

    Boolean existsByUserIdAndPlanId(Long userId, Long planId);

    Slice<UserPlan> findByPlanId(Long planId, PageRequest pageRequest);
}
