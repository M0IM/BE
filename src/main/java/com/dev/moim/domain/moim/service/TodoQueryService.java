package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.moim.dto.todo.TodoDetailDTO;
import com.dev.moim.domain.moim.dto.todo.TodoPageDTO;
import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.UserTodo;

import java.util.List;
import java.util.Optional;

public interface TodoQueryService {

    TodoDetailDTO getTotalDetailForAssignee(UserMoim userMoim, Long todoId);

    TodoDetailDTO getTodoDetailForAdmin(Long todoId);

    TodoPageDTO getTodoAssigneeListForAdmin(Long todoId, Long cursor, Integer take);

    TodoPageDTO getTodoNonAssigneeListForAdmin(Long moidId, Long todoId, Long cursor, Integer take);

    TodoPageDTO getMoimTodoListForAdmin(Long moimId, Long cursor, Integer take);

    TodoPageDTO getSpecificMoimTodoListByMe(UserMoim userMoim, Long moimId, Long cursor, Integer take);

    TodoPageDTO getAssignedTodoListForUserInSpecificMoim(UserMoim userMoim, Long moimId, Long cursor, Integer take);

    TodoPageDTO getTodoListByMe(UserMoim userMoim, Long cursor, Integer take);

    boolean existsByUserIdAndTodoId(Long userId, Long todoId);

    boolean existsByTodoId(Long todoId);

    Optional<UserTodo> findByUserIdAndTodoId(Long userId, Long todoId);

    Optional<Todo> findTodoByTodoId(Long todoId);

    List<UserTodo> findAssigneeByTodoId(Long todoId);
}
