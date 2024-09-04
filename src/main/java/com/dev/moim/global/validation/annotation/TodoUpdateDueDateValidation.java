package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.TodoUpdateDueDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodoUpdateDueDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TodoUpdateDueDateValidation {
    String message() default "todo 마감 기한을 현재 날짜 이전으로 수정할 수 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
