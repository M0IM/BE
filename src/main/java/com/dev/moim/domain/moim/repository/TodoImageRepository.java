package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.TodoImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoImageRepository extends JpaRepository<TodoImage, Long> {
}