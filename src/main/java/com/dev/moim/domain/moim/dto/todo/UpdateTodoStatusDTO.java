package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.global.validation.annotation.TodoAssigneeStatusValidation;
import com.dev.moim.global.validation.annotation.TodoAssigneeValidation;

@TodoAssigneeStatusValidation
public record UpdateTodoStatusDTO(
        @TodoAssigneeValidation Long todoId,
        TodoAssigneeStatus todoAssigneeStatus
) {
}
