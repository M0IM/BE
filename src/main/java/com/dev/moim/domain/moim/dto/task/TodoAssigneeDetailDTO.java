package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;

public record TodoAssigneeDetailDTO(
        Long assigneeId,
        String nickname,
        String profileImageUrl,
        TodoStatus todoStatus
) {
    public static TodoAssigneeDetailDTO of(UserTodo userTodo, UserMoim userMoim) {
        return new TodoAssigneeDetailDTO(
                userTodo.getUser().getId(),
                userMoim.getUserProfile().getName(),
                userMoim.getUserProfile().getImageUrl(),
                userTodo.getStatus()
        );
    }
}
