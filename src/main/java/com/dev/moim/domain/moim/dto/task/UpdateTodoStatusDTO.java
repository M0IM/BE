package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import com.dev.moim.global.validation.annotation.TodoStatusValidation;

@TodoStatusValidation
public record UpdateTodoStatusDTO(
        Long todoId,
        TodoStatus todoStatus
) {
}
