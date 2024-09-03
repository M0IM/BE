package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.dto.task.AddTodoAssigneeDTO;
import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusResponseDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.TodoCommandService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.TodoException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.firebase.service.FcmService;
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
    private final AlarmService alarmService;
    private final FcmService fcmService;

    @Override
    public Long createTodo(User user, Long moimId, CreateTodoDTO request) {

        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new MoimException(MOIM_NOT_FOUND));

        Todo todo = Todo.builder()
                .title(request.title())
                .content(request.content())
                .dueDate(request.dueDate().atTime(23, 59, 59, 999999))
                .status(TodoStatus.IN_PROGRESS)
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
                        .status(TodoAssigneeStatus.PENDING)
                        .build())
                .toList();

        userTodoRepository.saveAll(userTodoList);

        userList.stream().filter(assignee -> !user.equals(assignee))
                .forEach(assignee -> {

            alarmService.saveAlarm(user, assignee, "새로운 todo를 확인하고 작업을 시작해주세요.", todo.getTitle(), AlarmType.PUSH, AlarmDetailType.TODO, moim.getId(), null, null);

            if (assignee.getIsPushAlarm() && assignee.getDeviceId() != null) {
                fcmService.sendNotification(assignee, "새로운 todo를 확인하고 작업을 시작해주세요.", todo.getTitle());
            }
        });

        return todo.getId();
    }

    @Override
    public UpdateTodoStatusResponseDTO updateUserTodoStatus(User user, Long todoId, UpdateTodoStatusDTO request) {

        UserTodo userTodo = userTodoRepository.findByUserIdAndTodoId(user.getId(), todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        userTodo.updateStatus(request.todoAssigneeStatus());

        Todo todo = userTodo.getTodo();

        todo.updateStatus(
                userTodoRepository.findAllByTodoId(todoId).stream()
                        .map(UserTodo::getStatus)
                        .allMatch(status -> status == TodoAssigneeStatus.COMPLETE)
                ? TodoStatus.COMPLETED : TodoStatus.IN_PROGRESS
        );

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
                request.dueDate().atTime(23, 59, 59, 999999),
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
                            .status(TodoAssigneeStatus.LOADING)
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

    @Override
    public void addAssignee(Long todoId, AddTodoAssigneeDTO request) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        List<User> userList = userRepository.findAllById(request.addAssigneeIdList());

        List<UserTodo> userTodoListToAdd = userList.stream()
                .map(userEntity -> UserTodo.builder()
                        .user(userEntity)
                        .todo(todo)
                        .status(TodoAssigneeStatus.PENDING)
                        .build())
                .toList();

        todo.getUserTodoList().addAll(userTodoListToAdd);
    }
}
