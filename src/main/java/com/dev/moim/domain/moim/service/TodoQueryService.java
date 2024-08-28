package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.TodoDetailDTO;

public interface TodoQueryService {

    TodoDetailDTO getAssigneeTodoDetail(User user, Long todoId);

    boolean existsByUserIdAndTodoId(Long userId, Long todoId);

    boolean existsByTodoId(Long todoId);
}
