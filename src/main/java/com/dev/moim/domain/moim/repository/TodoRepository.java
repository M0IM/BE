package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    boolean existsById(Long todoId);

    @Query("SELECT t FROM Todo t WHERE t.moim.id = :moimId AND t.id < :cursor ORDER BY t.id DESC")
    Slice<Todo> findByMoimIdAndCursorLessThan(Long moimId, Long cursor, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.writer.id = :writerId AND t.moim.id = :moimId AND t.id < :cursor ORDER BY t.id DESC")
    Slice<Todo> findByWriterIdAndMoimIdAndCursorLessThan(Long writerId, Long moimId, Long cursor, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.writer.id = :writerId AND t.id < :cursor ORDER BY t.id DESC")
    Slice<Todo> findByWriterIdAndCursorLessThan(Long writerId, Long cursor, Pageable pageable);

    List<Todo> findAllByStatusAndDueDateBefore(TodoStatus todoStatus, LocalDateTime now);
}
