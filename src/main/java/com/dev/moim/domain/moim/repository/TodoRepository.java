package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
