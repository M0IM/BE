package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    boolean existsById(Long todoId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.userMoim " +
            "WHERE t.moim.id = :moimId AND t.id < :cursor ORDER BY t.id DESC")
    Slice<Todo> findByMoimIdAndCursorLessThanWithUserMoim(
            @Param("moimId") Long moimId,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.userMoim.id = :userMoimId AND t.moim.id = :moimId AND t.id < :cursor ORDER BY t.id DESC")
    Slice<Todo> findByUserMoimIdAndMoimIdAndCursorLessThan(
            @Param("userMoimId") Long userMoimId,
            @Param("moimId") Long moimId,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.userMoim.id = :userMoimId AND t.id < :cursor ORDER BY t.id DESC")
    Slice<Todo> findByUserMoimIdAndCursorLessThan(Long userMoimId, Long cursor, Pageable pageable);

    List<Todo> findAllByStatusAndDueDateBefore(TodoStatus todoStatus, LocalDateTime now);

    List<Todo> findAllByDueDateBetween(LocalDateTime tomorrow, LocalDateTime endOfTomorrow);

    @Query("SELECT t FROM Todo t " +
            "WHERE t.userMoim.id IN :userMoimIdList " +
            "AND t.id < :cursor " +
            "ORDER BY t.id DESC")
    Slice<Todo> findByUserMoimIdInWithPageable(
            @Param("userMoimIdList") List<Long> userMoimIdList,
            @Param("cursor") Long cursor,
            Pageable pageable);
}
