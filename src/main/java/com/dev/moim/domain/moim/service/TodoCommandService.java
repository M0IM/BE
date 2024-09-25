package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.todo.*;

public interface TodoCommandService {

    Long createTodo(User user, Long moimId, CreateTodoDTO request);

    UpdateTodoStatusResponseDTO updateUserTodoStatus(User user, Long todoId, UpdateTodoStatusDTO request);

    void updateTodo(User user, Long moimId, Long todoId, UpdateTodoDTO request);

    void deleteTodo(Long todoId);

    void addAssignees(User user, AddTodoAssigneeDTO request);

    void deleteAssignees(DeleteTodoAssigneeDTO request);

    void updateExpiredTodosAndAssigneesStatus();
}
