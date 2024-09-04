package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.dto.todo.UpdateTodoStatusDTO;
import com.dev.moim.domain.moim.entity.Todo;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.entity.enums.TodoAssigneeStatus;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.validation.annotation.TodoAssigneeStatusValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TodoAssigneeStatusValidator implements ConstraintValidator<TodoAssigneeStatusValidation, UpdateTodoStatusDTO> {

    private final TodoQueryService todoQueryService;

    @Override
    public void initialize(TodoAssigneeStatusValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateTodoStatusDTO request, ConstraintValidatorContext context) {

        Optional<Todo> todo = todoQueryService.findTodoByTodoId(request.todoId());
        if (todo.isEmpty()) {
            addConstraintViolation(context, TODO_NOT_FOUND.getMessage(), "todoId");
            return false;
        }

        if (todo.get().getDueDate().isBefore(LocalDateTime.now())) {
            addConstraintViolation(context, TODO_DUE_DATE_EXPIRED.getMessage(), "todoId");
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserTodo> userTodo = todoQueryService.findByUserIdAndTodoId(Long.valueOf(authentication.getName()), request.todoId());
        if (userTodo.isEmpty()) {
            addConstraintViolation(context, NOT_TODO_ASSIGNEE.getMessage(), "userId");
            return false;
        }

        if (request.todoAssigneeStatus() == TodoAssigneeStatus.OVERDUE) {
            addConstraintViolation(context, TODO_INVALID_STATE_REQUEST.getMessage(), "todoStatus");
            return false;
        }
        else if (request.todoAssigneeStatus() == userTodo.get().getStatus()) {
            addConstraintViolation(context, TODO_STATUS_SAME.getMessage(), "todoStatus");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}
