package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.dto.task.UpdateTodoStatusDTO;
import com.dev.moim.domain.moim.entity.UserTodo;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.error.handler.TodoException;
import com.dev.moim.global.validation.annotation.TodoStatusValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.NOT_TODO_ASSIGNEE;
import static com.dev.moim.global.common.code.status.ErrorStatus.TODO_STATUS_SAME;

@RequiredArgsConstructor
@Component
public class TodoStatusValidator implements ConstraintValidator<TodoStatusValidation, UpdateTodoStatusDTO> {

    private final TodoQueryService todoQueryService;

    @Override
    public void initialize(TodoStatusValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateTodoStatusDTO request, ConstraintValidatorContext context) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserTodo userTodo = todoQueryService.findByUserIdAndTodoId(Long.valueOf(authentication.getName()), request.todoId());

            if (userTodo.getStatus() == request.todoStatus()) {
                addConstraintViolation(context, TODO_STATUS_SAME.getMessage(), "todoStatus");
                return false;
            }
            return true;
        } catch (TodoException e) {
            addConstraintViolation(context, NOT_TODO_ASSIGNEE.getMessage(), "userId");
            return false;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}
