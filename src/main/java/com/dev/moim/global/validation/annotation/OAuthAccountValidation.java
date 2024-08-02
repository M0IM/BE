package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.OAuthAccountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OAuthAccountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OAuthAccountValidation {
    String message() default "이미 가입된 소셜 계정입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
