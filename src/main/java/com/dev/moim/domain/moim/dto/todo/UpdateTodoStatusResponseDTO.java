package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;

public record UpdateTodoStatusResponseDTO(
        Long todoId,
        TodoAssigneeStatus todoAssigneeStatus,
        TodoStatus todoStatus
) {
    public static UpdateTodoStatusResponseDTO of(UserTodo userTodo) {
        return new UpdateTodoStatusResponseDTO(
                userTodo.getId(),
                userTodo.getStatus(),
                userTodo.getTodo().getStatus()
        );
    }
}
