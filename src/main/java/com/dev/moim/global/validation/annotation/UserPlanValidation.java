package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.UserPlanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserPlanValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPlanValidation {
    String message() default "유저가 참여하지 않는 일정입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
