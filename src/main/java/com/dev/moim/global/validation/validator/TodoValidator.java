package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.validation.annotation.TodoValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.TODO_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class TodoValidator implements ConstraintValidator<TodoValidation, Long> {

    private final TodoQueryService todoQueryService;

    @Override
    public void initialize(TodoValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long todoId, ConstraintValidatorContext context) {
        boolean isExistTodo = todoQueryService.existsByTodoId(todoId);

        if (!isExistTodo) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(TODO_NOT_FOUND.toString())
                    .addPropertyNode("todoId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}