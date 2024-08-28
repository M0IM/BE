package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusDTO;
import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusResponseDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.TodoImageRepository;
import com.dev.moim.domain.moim.repository.TodoRepository;
import com.dev.moim.domain.moim.repository.UserTodoRepository;
import com.dev.moim.domain.moim.service.TodoCommandService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.TodoException;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        request.imageKeyList().forEach((i) ->{
                    TodoImage todoImage = TodoImage.builder().imageUrl
                            (i == null || i.isEmpty() || i.isBlank() ? null : s3Service.generateStaticUrl(i)).todo(todo)
                            .build();
                    todoImageRepository.save(todoImage);});

        List<User> userList = userRepository.findAllById(request.targetUserIdList());

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
}
