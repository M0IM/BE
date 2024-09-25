package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.global.validation.annotation.CheckAdminValidation;
import com.dev.moim.global.validation.annotation.DeleteAssigneeValidation;
import com.dev.moim.global.validation.annotation.MoimValidation;
import com.dev.moim.global.validation.annotation.TodoValidation;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@DeleteAssigneeValidation
public record DeleteTodoAssigneeDTO(
        @CheckAdminValidation @MoimValidation Long moimId,
        @TodoValidation Long todoId,
        @NotNull List<Long> deleteAssigneeIdList
) {
}
