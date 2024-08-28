package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.UserTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {

    Optional<UserTodo> findByUserIdAndTodoId(Long userId, Long todoId);

    boolean existsByUserIdAndTodoId(Long userId, Long todoId);
}
