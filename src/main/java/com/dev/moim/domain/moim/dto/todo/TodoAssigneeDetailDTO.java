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
    public static TodoAssigneeDetailDTO toTodoAssignee(UserTodo userTodo) {
        return new TodoAssigneeDetailDTO(
                userTodo.getUserMoim().getId(),
                userTodo.getUserMoim().getNickname(),
                userTodo.getUserMoim().getImageUrl(),
                userTodo.getStatus()
        );
    }

    public static TodoAssigneeDetailDTO toTodoNonAssignee(UserMoim userMoim) {
        return new TodoAssigneeDetailDTO(
                userMoim.getId(),
                userMoim.getNickname(),
                userMoim.getImageUrl(),
                null
        );
    }
}
