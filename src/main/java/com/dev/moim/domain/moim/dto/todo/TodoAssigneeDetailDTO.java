package com.dev.moim.domain.moim.dto.todo;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;

public record TodoAssigneeDetailDTO(
        Long assigneeId,
        String nickname,
        String profileImageUrl,
        TodoAssigneeStatus todoAssigneeStatus
) {
    public static TodoAssigneeDetailDTO toTodoAssignee(UserTodo userTodo, UserMoim userMoim) {
        return new TodoAssigneeDetailDTO(
                userTodo.getUser().getId(),
                userMoim.getUserProfile().getName(),
                userMoim.getUserProfile().getImageUrl(),
                userTodo.getStatus()
        );
    }

    public static TodoAssigneeDetailDTO toTodoNonAssignee(UserMoim userMoim) {
        return new TodoAssigneeDetailDTO(
                userMoim.getUser().getId(),
                userMoim.getUserProfile().getName(),
                userMoim.getUserProfile().getImageUrl(),
                null
        );
    }
}
