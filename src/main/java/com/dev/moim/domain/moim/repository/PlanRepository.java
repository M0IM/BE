package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Plan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM UserPlan up JOIN up.plan p WHERE up.user = :user AND p.date BETWEEN :startOfDay AND :endOfDay ORDER BY p.date ASC")
    Slice<Plan> findByUserAndDateBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

    List<Plan> findByMoim(Moim moim);

    @Query(value = "SELECT p.id, p.title, p.date, p.location, p.location_detail, NULL as memo, m.name as moimName, 'MOIM_PLAN' as plan_type " +
            "FROM plan p " +
            "JOIN moim m ON p.moim_id = m.id " +
            "JOIN user_plan up ON p.id = up.plan_id " +
            "WHERE up.user_id = :userId " +
            "AND p.date BETWEEN :startOfDay AND :endOfDay " +
            "UNION " +
            "SELECT ip.id, ip.title, ip.date, ip.location, ip.location_detail, ip.memo, NULL as moimName, 'INDIVIDUAL_PLAN' as plan_type " +
            "FROM individual_plan ip " +
            "WHERE ip.user_id = :userId " +
            "AND ip.date BETWEEN :startOfDay AND :endOfDay " +
            "UNION " +
            "SELECT t.id, t.title, t.due_date as date, NULL as location, NULL as location_detail, t.content as memo, m.name as moimName, 'TODO_PLAN' as plan_type " +
            "FROM todo t " +
            "JOIN user_todo ut ON t.id = ut.todo_id " +
            "LEFT JOIN moim m ON t.moim_id = m.id " +
            "WHERE ut.user_id = :userId " +
            "AND t.due_date BETWEEN :startOfDay AND :endOfDay " +
            "ORDER BY date ASC " +
            "LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Object[]> findUserPlansAndIndividualPlans(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("size") int size,
            @Param("offset") int offset);
}
