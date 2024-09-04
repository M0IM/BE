package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.global.validation.annotation.TodoUpdateDueDateValidation;

import java.time.LocalDate;
import java.util.List;

public record UpdateTodoDTO(
        String title,
        String content,
        @TodoUpdateDueDateValidation LocalDate dueDate,
        List<String> imageKeyList
) {
}