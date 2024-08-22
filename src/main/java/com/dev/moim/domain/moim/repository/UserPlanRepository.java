package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.UserPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    List<UserPlan> findByUserIdAndPlanDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT up FROM UserPlan up " +
            "JOIN FETCH up.user u " +
            "JOIN FETCH UserMoim um ON u.id = um.user.id AND um.moim.id = :moimId " +
            "WHERE up.plan.id = :planId")
    Slice<UserPlan> findByPlanIdWithUserAndUserMoim(@Param("planId") Long planId,
                                                    @Param("moimId") Long moimId,
                                                    Pageable pageable);

    Optional<UserPlan> findByUserIdAndPlanId(Long userId, Long planId);

    @Query("SELECT COUNT(up) FROM UserPlan up WHERE up.user = :user AND up.plan.date BETWEEN :startOfDay AND :endOfDay")
    int countPlansByUserAndDateBetween(@Param("user") User user, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

}
