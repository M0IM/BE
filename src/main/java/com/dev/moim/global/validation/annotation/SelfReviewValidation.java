package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.SelfReviewValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SelfReviewValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelfReviewValidation {
    String message() default "유저 본인에게 리뷰를 남길 수 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

