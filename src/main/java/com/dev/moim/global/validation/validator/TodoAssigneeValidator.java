package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.validation.annotation.TodoAssigneeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.NOT_TODO_ASSIGNEE;
import static com.dev.moim.global.common.code.status.ErrorStatus.TODO_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class TodoAssigneeValidator implements ConstraintValidator<TodoAssigneeValidation, Long> {

    private final TodoQueryService todoQueryService;

    @Override
    public void initialize(TodoAssigneeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long todoId, ConstraintValidatorContext context) {

        boolean isExistTodo = todoQueryService.existsByTodoId(todoId);
        if (!isExistTodo) {
            addConstraintViolation(context, TODO_NOT_FOUND.toString());
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAssignee = todoQueryService.existsByUserIdAndTodoId(Long.valueOf(authentication.getName()), todoId);

        if (!isAssignee) {
            addConstraintViolation(context, NOT_TODO_ASSIGNEE.toString());
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("todoId")
                .addConstraintViolation();
    }
}

