package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.global.validation.annotation.MoimMemberListValidation;

import java.time.LocalDateTime;
import java.util.List;

@MoimMemberListValidation
public record CreateTodoDTO(
        Long moimId,
        String title,
        String content,
        LocalDateTime dueDate,
        List<String> imageKeyList,
        List<Long> targetUserIdList
) {
}
