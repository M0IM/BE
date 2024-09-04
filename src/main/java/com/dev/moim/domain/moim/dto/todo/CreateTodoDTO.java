package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.global.validation.annotation.TodoTargetUserValidation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@TodoTargetUserValidation
public record CreateTodoDTO(
        Long moimId,
        String title,
        String content,
        LocalDate dueDate,
        List<String> imageKeyList,
        List<Long> targetUserIdList,
        @Schema(description = "멤버 전체 선택 여부", defaultValue = "false")
        boolean isAssigneeSelectAll
) {
}
