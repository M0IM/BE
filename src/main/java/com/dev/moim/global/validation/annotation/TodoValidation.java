package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.TodoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodoValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TodoValidation {
    String message() default "존재하지 않는 todo입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

