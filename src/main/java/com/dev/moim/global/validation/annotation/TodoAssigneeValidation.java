package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.TodoAssigneeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodoAssigneeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TodoAssigneeValidation {
    String message() default "해당 유저는 해당 todo를 조회할 권한이 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

