package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.UserTodo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {

    Optional<UserTodo> findByUserIdAndTodoId(Long userId, Long todoId);

    boolean existsByUserIdAndTodoId(Long userId, Long todoId);

    @Query("SELECT ut FROM UserTodo ut " +
            "JOIN FETCH ut.user u " +
            "JOIN FETCH u.userMoimList um " +
            "JOIN FETCH um.userProfile up " +
            "WHERE ut.todo.id = :todoId " +
            "AND um.moim.id = ut.todo.moim.id " +
            "AND ut.id > :cursor " +
            "ORDER BY ut.id ASC")
    Slice<UserTodo> findAllWithUserMoimAndUserProfileByTodoIdAndCursor(
            @Param("todoId") Long todoId,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT COUNT(ut) " +
            "FROM UserTodo ut " +
            "JOIN ut.todo t " +
            "WHERE ut.user = :user " +
            "AND t.dueDate BETWEEN :startOfDay AND :endOfDay")
    int countByUserAndTodoDueDateBetween(@Param("user") User user,
                                         @Param("startOfDay") LocalDateTime startOfDay,
                                         @Param("endOfDay") LocalDateTime endOfDay);

    List<UserTodo> findAllByTodoId(Long todoId);

    @Query("SELECT ut FROM UserTodo ut " +
            "JOIN ut.todo t " +
            "WHERE ut.user.id = :userId " +
            "AND t.dueDate BETWEEN :startDate AND :endDate " +
            "ORDER BY t.dueDate ASC")
    List<UserTodo> findByUserIdAndTodoDueDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
