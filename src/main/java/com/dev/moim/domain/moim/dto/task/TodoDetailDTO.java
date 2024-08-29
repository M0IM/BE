package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.enums.TodoStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TodoDetailDTO(
        Long todoId,
        String title,
        String content,
        LocalDateTime dueDate,
        List<String> imageUrlList,
        TodoStatus status
) {
}
