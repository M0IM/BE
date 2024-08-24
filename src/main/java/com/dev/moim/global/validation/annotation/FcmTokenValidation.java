package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.FcmTokenValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FcmTokenValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FcmTokenValidation {
    String message() default "FCM 토큰이 유효하지 않습니다..";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
