package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.AddTodoAssigneeDTO;
import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusResponseDTO;

public interface TodoCommandService {

    Long createTodo(User user, Long moimId, CreateTodoDTO request);

    UpdateTodoStatusResponseDTO updateUserTodoStatus(User user, Long todoId, UpdateTodoStatusDTO request);

    void updateTodo(Long todoId, CreateTodoDTO request);

    void deleteTodo(Long todoId);

    void addAssignee(Long todoId, AddTodoAssigneeDTO request);
}
