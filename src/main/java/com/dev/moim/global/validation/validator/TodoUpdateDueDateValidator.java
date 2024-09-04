package com.dev.moim.global.validation.validator;

import com.dev.moim.global.validation.annotation.TodoUpdateDueDateValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_TODO_DUE_DATE;

@Slf4j
@RequiredArgsConstructor
@Component
public class TodoUpdateDueDateValidator implements ConstraintValidator<TodoUpdateDueDateValidation, LocalDate> {

    @Override
    public void initialize(TodoUpdateDueDateValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate dueDate, ConstraintValidatorContext context) {

        log.info("LocalDate.now() : {}", LocalDate.now());

        if (dueDate.isBefore(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INVALID_TODO_DUE_DATE.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
