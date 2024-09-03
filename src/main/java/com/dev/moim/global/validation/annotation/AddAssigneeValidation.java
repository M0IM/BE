package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.AddAssigneeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddAssigneeValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddAssigneeValidation {
    String message() default "todo를 할당받을 멤버가 유효하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
