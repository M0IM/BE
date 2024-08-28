package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.TodoAssigneeListForAdminDTO;
import com.dev.moim.domain.moim.dto.task.TodoDetailDTO;

public interface TodoQueryService {

    TodoDetailDTO getTotalDetailForAssignee(User user, Long todoId);

    TodoDetailDTO getTodoDetailForAdmin(Long todoId);

    TodoAssigneeListForAdminDTO getTodoAssigneeListForAdmin(Long todoId, Long cursor, Integer take);

    boolean existsByUserIdAndTodoId(Long userId, Long todoId);

    boolean existsByTodoId(Long todoId);
}
