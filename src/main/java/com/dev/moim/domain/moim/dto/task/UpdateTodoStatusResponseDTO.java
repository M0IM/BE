package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;

public record UpdateTodoStatusResponseDTO(
        Long todoId,
        TodoStatus todoStatus
) {
    public static UpdateTodoStatusResponseDTO of(UserTodo userTodo) {
        return new UpdateTodoStatusResponseDTO(
                userTodo.getId(),
                userTodo.getStatus()
        );
    }
}
