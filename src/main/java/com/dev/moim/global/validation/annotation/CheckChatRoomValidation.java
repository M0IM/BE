package com.dev.moim.global.validation.annotation;

import com.dev.moim.global.validation.validator.CheckChatRoomValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckChatRoomValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckChatRoomValidation {
}
