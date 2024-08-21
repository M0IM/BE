package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.PlanAuthorityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlanAuthorityValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PlanAuthorityValidation {
    String message() default "일정 수정, 삭제 권한이 있는 유저가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
