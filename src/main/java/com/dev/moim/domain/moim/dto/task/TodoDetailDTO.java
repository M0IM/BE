package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TodoDetailDTO(
        Long todoId,
        Long moimId,
        String title,
        String content,
        LocalDateTime dueDate,
        List<String> imageUrlList,
        TodoAssigneeStatus todoAssigneeStatus,
        TodoStatus todoStatus
) {
}
