package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;

public interface TodoCommandService {

    Long createTodo(User user, Long moimId, CreateTodoDTO request);
}
