package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusResponseDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.TodoCommandService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.TodoException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TodoCommandServiceImpl implements TodoCommandService {

    private final MoimRepository moimRepository;
    private final TodoRepository todoRepository;
    private final TodoImageRepository todoImageRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserTodoRepository userTodoRepository;
    private final UserMoimRepository userMoimRepository;

    @Override
    public Long createTodo(User user, Long moimId, CreateTodoDTO request) {

        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new MoimException(MOIM_NOT_FOUND));

        Todo todo = Todo.builder()
                .title(request.title())
                .content(request.content())
                .dueDate(request.dueDate())
                .moim(moim)
                .writer(user)
                .build();

        todoRepository.save(todo);

        request.imageKeyList().forEach((i) ->
                        todoImageRepository.save(TodoImage.builder()
                                .imageUrl((i == null || i.isEmpty() || i.isBlank()) ? null : s3Service.generateStaticUrl(i))
                                .todo(todo)
                                .build()));

        List<User> userList = request.isAssigneeSelectAll()
                ? userMoimRepository.findByMoimIdAndJoinStatus(moimId, JoinStatus.COMPLETE).stream()
                .map(UserMoim::getUser).toList()
                : userRepository.findAllById(request.targetUserIdList());

        List<UserTodo> userTodoList = userList.stream()
                .map(userEntity -> UserTodo.builder()
                        .user(userEntity)
                        .todo(todo)
                        .status(TodoStatus.LOADING)
                        .build())
                .toList();

        userTodoRepository.saveAll(userTodoList);

        return todo.getId();
    }

    @Override
    public UpdateTodoStatusResponseDTO updateUserTodoStatus(User user, Long todoId, UpdateTodoStatusDTO request) {

        UserTodo userTodo = userTodoRepository.findByUserIdAndTodoId(user.getId(), todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));
        userTodo.updateStatus(request.todoStatus());

        return UpdateTodoStatusResponseDTO.of(userTodo);
    }

    @Override
    public void updateTodo(Long todoId, CreateTodoDTO request) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        List<TodoImage> newImageList = request.imageKeyList().stream()
                .map(imageKey -> TodoImage.builder()
                        .imageUrl(s3Service.generateStaticUrl(imageKey))
                        .todo(todo)
                        .build())
                .toList();

        todo.updateTodo(
                request.title(),
                request.content(),
                request.dueDate(),
                newImageList
        );

        List<UserTodo> existingUserTodoList = todo.getUserTodoList();
        Set<Long> existingUserIdSet = existingUserTodoList.stream()
                .map(userTodo -> userTodo.getUser().getId())
                .collect(Collectors.toSet());

        Set<Long> requestUserIdSet = request.isAssigneeSelectAll()
                ? userMoimRepository.findByMoimIdAndJoinStatus(request.moimId(), JoinStatus.COMPLETE).stream()
                .map(userMoim -> userMoim.getUser().getId())
                .collect(Collectors.toSet())
                : new HashSet<>(request.targetUserIdList());

        List<UserTodo> userTodoListToAdd = requestUserIdSet.stream()
                .filter(userId -> !existingUserIdSet.contains(userId))
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
                    return UserTodo.builder()
                            .todo(todo)
                            .user(user)
                            .status(TodoStatus.LOADING)
                            .build();
                }).toList();
        List<UserTodo> userTodoListToRemove = existingUserTodoList.stream()
                .filter(userTodo -> !requestUserIdSet.contains(userTodo.getUser().getId()))
                .toList();

        todo.getUserTodoList().removeAll(userTodoListToRemove);
        todo.getUserTodoList().addAll(userTodoListToAdd);
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}
