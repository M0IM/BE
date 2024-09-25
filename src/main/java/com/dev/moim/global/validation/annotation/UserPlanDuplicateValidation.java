package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.UserPlanDuplicateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserPlanDuplicateValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPlanDuplicateValidation {
    String message() default "이미 해당 모임 일정에 참여 신청했습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
