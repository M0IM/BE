package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.TodoAssigneeDetailDTO;
import com.dev.moim.domain.moim.dto.task.TodoAssigneeListForAdminDTO;
import com.dev.moim.domain.moim.dto.task.TodoDetailDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.repository.TodoRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.repository.UserTodoRepository;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.TodoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TodoQueryServiceImpl implements TodoQueryService {

    private final UserTodoRepository userTodoRepository;
    private final TodoRepository todoRepository;
    private final UserMoimRepository userMoimRepository;

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
    public TodoAssigneeListForAdminDTO getTodoAssigneeListForAdmin(Long todoId, Long cursor, Integer take) {

        Long startCursor = (cursor == null) ? 0L : cursor;

        Slice<UserTodo> userTodoSlice = userTodoRepository.findAllWithUserMoimAndUserProfileByTodoIdAndCursor(todoId, startCursor, PageRequest.of(0, take));

        List<TodoAssigneeDetailDTO> todoAssigneeDetailDTOList = userTodoSlice.stream()
                .map(userTodo -> {
                    User user = userTodo.getUser();
                    Moim moim = userTodo.getTodo().getMoim();

                    UserMoim userMoim = user.getUserMoimList().stream()
                            .filter(um -> um.getMoim().equals(moim))
                            .findFirst()
                            .orElseThrow(() -> new MoimException(USER_MOIM_NOT_FOUND));

                    return TodoAssigneeDetailDTO.of(userTodo, userMoim);
                })
                .toList();

        Long nextCursor = userTodoSlice.hasNext() && !userTodoSlice.getContent().isEmpty()
                ? userTodoSlice.getContent().get(userTodoSlice.getNumberOfElements() - 1).getId()
                : null;

        return new TodoAssigneeListForAdminDTO(todoAssigneeDetailDTOList, nextCursor, userTodoSlice.hasNext());
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
