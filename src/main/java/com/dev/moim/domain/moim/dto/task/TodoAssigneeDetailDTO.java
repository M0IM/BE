package com.dev.moim.domain.moim.dto.task;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;

public record TodoAssigneeDetailDTO(
        Long assigneeId,
        String nickname,
        String profileImageUrl,
        TodoAssigneeStatus todoAssigneeStatus
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
