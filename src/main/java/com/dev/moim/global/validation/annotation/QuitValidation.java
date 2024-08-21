package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.QuitValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuitValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuitValidation {
    String message() default "모임장 권한이 있는 모임이 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}