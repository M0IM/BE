package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.global.validation.annotation.TodoAssigneeStatusValidation;

@TodoAssigneeStatusValidation
public record UpdateTodoStatusDTO(
        Long todoId,
        TodoAssigneeStatus todoAssigneeStatus
) {
}
