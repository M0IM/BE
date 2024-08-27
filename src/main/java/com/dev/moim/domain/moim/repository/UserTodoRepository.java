package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.UserTodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {
}
