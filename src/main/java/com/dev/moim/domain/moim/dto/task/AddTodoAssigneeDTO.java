package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.global.validation.annotation.AddAssigneeValidation;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@AddAssigneeValidation
public record AddTodoAssigneeDTO(
        Long moimId,
        Long todoId,
        @NotNull List<Long> addAssigneeIdList
) {
}
