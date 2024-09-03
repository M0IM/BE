package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.*;

public interface TodoCommandService {

    Long createTodo(User user, Long moimId, CreateTodoDTO request);

    UpdateTodoStatusResponseDTO updateUserTodoStatus(User user, Long todoId, UpdateTodoStatusDTO request);

    void updateTodo(Long todoId, UpdateTodoDTO request);

    void deleteTodo(Long todoId);

    void addAssignees(AddTodoAssigneeDTO request);

    void deleteAssignees(DeleteTodoAssigneeDTO request);
}
