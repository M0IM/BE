package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.moim.dto.todo.TodoAssigneeDetailDTO;
import com.dev.moim.domain.moim.dto.todo.TodoDTO;
import com.dev.moim.domain.moim.dto.todo.TodoDetailDTO;
import com.dev.moim.domain.moim.dto.todo.TodoPageDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.repository.TodoRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.repository.UserTodoRepository;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.error.handler.TodoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public TodoDetailDTO getTotalDetailForAssignee(UserMoim userMoim, Long todoId) {

        UserTodo userTodo = userTodoRepository.findByUserMoimIdAndTodoIdWithTodo(userMoim.getId(), todoId)
                .orElseThrow(() -> new TodoException(NOT_TODO_ASSIGNEE));

        return new TodoDetailDTO(
                userTodo.getTodo().getId(),
                userTodo.getTodo().getMoim().getId(),
                userTodo.getTodo().getTitle(),
                userTodo.getTodo().getContent(),
                userTodo.getTodo().getDueDate(),
                userTodo.getTodo().getTodoImageList().stream().map(TodoImage::getImageUrl).toList(),
                userTodo.getStatus(),
                userTodo.getTodo().getStatus()
        );
    }

    @Override
    public TodoDetailDTO getTodoDetailForAdmin(Long todoId) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        return new TodoDetailDTO(
                todo.getId(),
                todo.getMoim().getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.getDueDate(),
                todo.getTodoImageList().stream().map(TodoImage::getImageUrl).toList(),
                null,
                todo.getStatus()
        );
    }

    @Override
    public TodoPageDTO getTodoAssigneeListForAdmin(Long todoId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? 0L : cursor;
        Pageable pageable = PageRequest.of(0, take);

        Slice<UserTodo> userTodoSlice = userTodoRepository.findAllWithUserMoimAndUserProfileByTodoIdAndCursor(todoId, startCursor, pageable);

        List<TodoAssigneeDetailDTO> todoAssigneeDetailDTOList = userTodoSlice.stream()
                .map(TodoAssigneeDetailDTO::toTodoAssignee)
                .toList();

        Long nextCursor = userTodoSlice.hasNext() && !userTodoSlice.getContent().isEmpty()
                ? userTodoSlice.getContent().get(userTodoSlice.getNumberOfElements() - 1).getId()
                : null;

        return new TodoPageDTO(todoAssigneeDetailDTOList, nextCursor, userTodoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getTodoNonAssigneeListForAdmin(Long moimId, Long todoId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? 0L : cursor;
        Pageable pageable = PageRequest.of(0, take);

        Slice<UserMoim> userMoimSlice = userMoimRepository.findAllMembersNotAssignedToTodo(moimId, todoId, JoinStatus.COMPLETE, startCursor, pageable);

        List<TodoAssigneeDetailDTO> todoAssigneeDetailDTOList = userMoimSlice.stream()
                .map(TodoAssigneeDetailDTO::toTodoNonAssignee)
                .toList();

        Long nextCursor = userMoimSlice.hasNext() && !userMoimSlice.getContent().isEmpty()
                ? userMoimSlice.getContent().get(userMoimSlice.getNumberOfElements() - 1).getId()
                : null;

        return new TodoPageDTO(todoAssigneeDetailDTOList, nextCursor, userMoimSlice.hasNext());
    }

    @Override
    public TodoPageDTO getMoimTodoListForAdmin(Long moimId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<Todo> todoSlice = todoRepository.findByMoimIdAndCursorLessThanWithUserMoim(moimId, startCursor, pageable);

        List<TodoDTO> todoDTOList = todoSlice.stream()
                .map(TodoDTO::forMoimAdmins)
                .toList();

        Long nextCursor = todoSlice.hasNext() ? todoSlice.getContent().get(todoSlice.getNumberOfElements() - 1).getId() : null;

        return new TodoPageDTO(todoDTOList, nextCursor, todoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getSpecificMoimTodoListByMe(UserMoim userMoim, Long moimId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<Todo> todoSlice = todoRepository.findByUserMoimIdAndMoimIdAndCursorLessThan(userMoim.getId(), moimId, startCursor, pageable);

        List<TodoDTO> todoDTOList = todoSlice.getContent().stream()
                .map(TodoDTO::forSpecificAdmin)
                .toList();

        Long nextCursor = todoSlice.hasNext() ? todoSlice.getContent().get(todoSlice.getNumberOfElements() - 1).getId() : null;

        return new TodoPageDTO(todoDTOList, nextCursor, todoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getAssignedTodoListForUserInSpecificMoim(UserMoim userMoim, Long moimId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<UserTodo> userTodoSlice = userTodoRepository.findUserTodosByUserMoimIdAndMoimId(userMoim.getId(), moimId, startCursor, pageable);

        List<TodoDTO> todoDTOList = userTodoSlice.getContent().stream()
                .map(userTodo -> TodoDTO.forAssignee(userTodo.getTodo(), userTodo))
                .toList();

        Long nextCursor = userTodoSlice.hasNext() ? userTodoSlice.getContent().get(userTodoSlice.getNumberOfElements() - 1).getId() : null;

        return new TodoPageDTO(todoDTOList, nextCursor, userTodoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getTodoListByMe(UserMoim userMoim, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<Todo> todoSlice = todoRepository.findByUserMoimIdAndCursorLessThan(userMoim.getId(), startCursor, pageable);

        List<TodoDTO> todoDTOList = todoSlice.getContent().stream()
                .map(TodoDTO::forSpecificAdmin)
                .toList();

        Long nextCursor = todoSlice.hasNext() ? todoSlice.getContent().get(todoSlice.getNumberOfElements() - 1).getId() : null;

        return new TodoPageDTO(todoDTOList, nextCursor, todoSlice.hasNext());
    }

    @Override
    public boolean existsByUserIdAndTodoId(Long userId, Long todoId) {
        return userTodoRepository.existsByUserIdAndTodoId(userId, todoId);
    }

    @Override
    public boolean existsByTodoId(Long todoId) {
        return todoRepository.existsById(todoId);
    }

    @Override
    public Optional<UserTodo> findByUserIdAndTodoId(Long userId, Long todoId) {
        return userTodoRepository.findByUserIdAndTodoId(userId, todoId);
    }

    @Override
    public Optional<Todo> findTodoByTodoId(Long todoId) {
        return todoRepository.findById(todoId);
    }

    @Override
    public List<UserTodo> findAssigneeByTodoId(Long todoId) {
        return userTodoRepository.findAllByTodoId(todoId);
    }
}
