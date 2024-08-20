package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.JoinPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JoinPasswordValidator.class)
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinPasswordValidation {
    String message() default "비밀번호 조건에 맞지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
