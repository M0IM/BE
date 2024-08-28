package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.TodoDetailDTO;
import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.TodoImage;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.repository.TodoRepository;
import com.dev.moim.domain.moim.repository.UserTodoRepository;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.error.handler.TodoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moim.global.common.code.status.ErrorStatus.NOT_TODO_ASSIGNEE;
import static com.dev.moim.global.common.code.status.ErrorStatus.TODO_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TodoQueryServiceImpl implements TodoQueryService {

    private final UserTodoRepository userTodoRepository;
    private final TodoRepository todoRepository;

    @Override
    public TodoDetailDTO getTotalDetailForAssignee(User user, Long todoId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        UserTodo userTodo = userTodoRepository.findByUserIdAndTodoId(user.getId(), todoId)
                .orElseThrow(() -> new TodoException(NOT_TODO_ASSIGNEE));

        return new TodoDetailDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.getDueDate(),
                todo.getTodoImageList().stream().map(TodoImage::getImageUrl).toList(),
                userTodo.getStatus()
        );
    }

    @Override
    public TodoDetailDTO getTodoDetailForAdmin(Long todoId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        return new TodoDetailDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.getDueDate(),
                todo.getTodoImageList().stream().map(TodoImage::getImageUrl).toList(),
                null
        );
    }

    @Override
    public boolean existsByUserIdAndTodoId(Long userId, Long todoId) {
        return userTodoRepository.existsByUserIdAndTodoId(userId, todoId);
    }

    @Override
    public boolean existsByTodoId(Long todoId) {
        return todoRepository.existsById(todoId);
    }
}
