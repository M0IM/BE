package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.global.validation.annotation.TodoAssigneeStatusValidation;

@TodoAssigneeStatusValidation
public record UpdateTodoStatusDTO(
        Long todoId,
        TodoAssigneeStatus todoAssigneeStatus
) {
}
