package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.MoimValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MoimValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MoimValidation {
    String message() default "존재하지 않는 모임입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}