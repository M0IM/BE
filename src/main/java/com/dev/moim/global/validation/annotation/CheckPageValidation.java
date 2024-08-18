package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.CheckPageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckPageValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPageValidation {
    String message() default "허용 되지 않은 page 값 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
