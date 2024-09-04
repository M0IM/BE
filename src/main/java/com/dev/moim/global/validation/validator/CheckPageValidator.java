package com.dev.moim.global.validation.validator;

import com.dev.moim.global.validation.annotation.CheckPageValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.NOT_VALID_PAGE;

@Component
@RequiredArgsConstructor
public class CheckPageValidator implements ConstraintValidator<CheckPageValidation, Integer> {
    @Override
    public void initialize(CheckPageValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(NOT_VALID_PAGE.getMessage())
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
