package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.moim.dto.task.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.TodoCommandService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.TodoException;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
                .dueDate(request.dueDate().atTime(23, 59, 59, 999999000))
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

        userList.stream().filter(assignee -> !assignee.equals(user))
                .forEach(assignee -> {
                    alarmService.saveAlarm(user, assignee, "새로운 할 일이 도착했습니다", todo.getTitle(), AlarmType.PUSH, AlarmDetailType.TODO, moim.getId(), null, null);

                    if (assignee.getIsPushAlarm() && assignee.getDeviceId() != null) {
                        fcmService.sendNotification(assignee, "새로운 할 일이 도착했습니다", todo.getTitle());
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
    public void updateTodo(User user, Long moimId, Long todoId, UpdateTodoDTO request) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        LocalDateTime dueDateTime = request.dueDate().atTime(23, 59, 59, 999999000);

        for (UserTodo userTodo : todo.getUserTodoList()) {
            if (!dueDateTime.equals(todo.getDueDate())) {
                if (dueDateTime.isAfter(LocalDateTime.now())) {
                    userTodo.updateStatus(switch (userTodo.getStatus()) {
                        case LOADING, COMPLETE -> TodoAssigneeStatus.LOADING;
                        case OVERDUE -> TodoAssigneeStatus.PENDING;
                        default -> userTodo.getStatus();
                    });
                }
            } else {
                userTodo.updateStatus(switch (userTodo.getStatus()) {
                    case LOADING, COMPLETE -> TodoAssigneeStatus.LOADING;
                    case OVERDUE -> TodoAssigneeStatus.PENDING;
                    default -> userTodo.getStatus();
                });
            }
        }

        List<TodoImage> newImageList = request.imageKeyList().stream()
                .map(imageKey -> TodoImage.builder()
                        .imageUrl(s3Service.generateStaticUrl(imageKey))
                        .todo(todo)
                        .build())
                .toList();

        todo.updateTodo(
                request.title(),
                request.content(),
                dueDateTime,
                newImageList
        );

        todo.getUserTodoList().stream().map(UserTodo::getUser).filter(assignee -> !assignee.equals(user))
                .forEach(assignee -> {
                    alarmService.saveAlarm(user, assignee, "할 일이 수정되었습니다", todo.getTitle(), AlarmType.PUSH, AlarmDetailType.TODO, moimId, null, null);

                    if (assignee.getIsPushAlarm() && assignee.getDeviceId() != null) {
                        fcmService.sendNotification(assignee, "할 일이 수정되었습니다", todo.getTitle());
                    }
                });
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }

    @Override
    public void addAssignees(User user, AddTodoAssigneeDTO request) {

        Todo todo = todoRepository.findById(request.todoId())
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

        userTodoListToAdd.stream().map(UserTodo::getUser).filter(assignee -> !assignee.equals(user))
                .forEach(assignee -> {
                    alarmService.saveAlarm(user, assignee, "새로운 할 일이 도착했습니다", todo.getTitle(), AlarmType.PUSH, AlarmDetailType.TODO, request.moimId(), null, null);

                    if (assignee.getIsPushAlarm() && assignee.getDeviceId() != null) {
                        fcmService.sendNotification(assignee, "새로운 할 일이 도착했습니다", todo.getTitle());
                    }
                });
    }

    @Override
    public void deleteAssignees(DeleteTodoAssigneeDTO request) {

        Todo todo = todoRepository.findById(request.todoId())
                .orElseThrow(() -> new TodoException(TODO_NOT_FOUND));

        List<UserTodo> userTodoList = request.deleteAssigneeIdList().stream()
                .map(id -> userTodoRepository.findByUserIdAndTodoId(id, request.todoId())
                        .orElseThrow(() -> new TodoException(NOT_TODO_ASSIGNEE))).toList();

        todo.getUserTodoList().removeAll(userTodoList);
    }
}
