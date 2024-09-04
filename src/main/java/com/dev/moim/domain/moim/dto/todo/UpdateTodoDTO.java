package com.dev.moim.domain.moim.dto.todo;

import java.time.LocalDate;
import java.util.List;

public record UpdateTodoDTO(
        String title,
        String content,
        LocalDate dueDate,
        List<String> imageKeyList
) {
}
