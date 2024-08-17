package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.IndividualPlanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IndividualPlanValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IndividualPlanValidation {
    String message() default "해당 일정의 작성자가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
