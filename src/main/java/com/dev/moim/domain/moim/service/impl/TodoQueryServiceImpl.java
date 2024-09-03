package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.task.TodoAssigneeDetailDTO;
import com.dev.moim.domain.moim.dto.task.TodoDTO;
import com.dev.moim.domain.moim.dto.task.TodoDetailDTO;
import com.dev.moim.domain.moim.dto.task.TodoPageDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                userTodo.getStatus(),
                todo.getStatus()
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

        return new TodoPageDTO(todoAssigneeDetailDTOList, nextCursor, userTodoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getMoimTodoListForAdmin(Long moimId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<Todo> todoSlice = todoRepository.findByMoimIdAndCursorLessThan(moimId, startCursor, pageable);

        Set<Long> writerIds = todoSlice.getContent().stream()
                .map(todo -> todo.getWriter().getId())
                .collect(Collectors.toSet());
        List<UserMoim> userMoimList = userMoimRepository.findByMoimIdAndUserIds(moimId, writerIds);
        Map<Long, UserMoim> userMoimMap = userMoimList.stream()
                .collect(Collectors.toMap(um -> um.getUser().getId(), um -> um));

        List<TodoDTO> todoDTOList = todoSlice.getContent().stream()
                .map(todo -> {
                    Long writerId = todo.getWriter().getId();
                    UserMoim userMoim = userMoimMap.get(writerId);
                    return TodoDTO.forMoimAdmins(todo, userMoim);
                })
                .toList();

        Long nextCursor = todoSlice.hasNext() ? todoSlice.getContent().get(todoSlice.getNumberOfElements() - 1).getId() : null;

        return new TodoPageDTO(todoDTOList, nextCursor, todoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getSpecificMoimTodoListByMe(User user, Long moimId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<Todo> todoSlice = todoRepository.findByWriterIdAndMoimIdAndCursorLessThan(user.getId(), moimId, startCursor, pageable);

        List<TodoDTO> todoDTOList = todoSlice.getContent().stream()
                .map(TodoDTO::forSpecificAdmin)
                .toList();

        Long nextCursor = todoSlice.hasNext() ? todoSlice.getContent().get(todoSlice.getNumberOfElements() - 1).getId() : null;

        return new TodoPageDTO(todoDTOList, nextCursor, todoSlice.hasNext());
    }

    @Override
    public TodoPageDTO getTodoListByMe(User user, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? Long.MAX_VALUE : cursor;
        Pageable pageable = PageRequest.of(0, take, Sort.by(Sort.Order.desc("id")));

        Slice<Todo> todoSlice = todoRepository.findByWriterIdAndCursorLessThan(user.getId(), startCursor, pageable);

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
}
