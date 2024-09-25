package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.CheckTakeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CheckTakeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTakeValidation {
    String message() default "허용 되지 않은 take 값 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
