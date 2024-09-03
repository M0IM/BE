package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.TodoAssigneeStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodoAssigneeStatusValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TodoAssigneeStatusValidation {
    String message() default "todo status가 유효하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
