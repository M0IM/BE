package com.dev.moim.global.validation.validator;

import com.dev.moim.global.validation.annotation.ExistUserValidation;
import com.dev.moim.global.validation.annotation.SelfReviewValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.SELF_REVIEW_FORBIDDEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class SelfReviewValidator implements ConstraintValidator<SelfReviewValidation, Long> {

    @Override
    public void initialize(SelfReviewValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(@ExistUserValidation Long targetUserId, ConstraintValidatorContext context) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (targetUserId.toString().equals(authentication.getName())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(SELF_REVIEW_FORBIDDEN.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}